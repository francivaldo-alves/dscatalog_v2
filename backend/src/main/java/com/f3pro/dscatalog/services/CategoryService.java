package com.f3pro.dscatalog.services;


import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import com.f3pro.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    // Busca todas as categorias
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll().
                stream().map(CategoryDTO::new).toList();

    }


    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(findEntityById(id));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        logger.info("Criando uma categoria:{}", dto.getName());
        Category entity = new Category();
       copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        logger.info("Categoria criada com id:{}", entity.getId());
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        logger.info("Atualizando uma categoria id:{}", id);
        Category entity = findEntityById(id);
        copyDtoToEntity(dto, entity);
        return new CategoryDTO(repository.save(entity));

    }
    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================
    private Category findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada para o id " + id)
                );
    }


    private void copyDtoToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.getName());
    }


}
