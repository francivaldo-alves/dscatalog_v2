package com.f3pro.dscatalog.services;

import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import com.f3pro.dscatalog.services.exceptions.DatabaseException;
import com.f3pro.dscatalog.services.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    // ==========================
    // CONSULTAS
    // ==========================

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(findEntityById(id));
    }

    // ==========================
    // COMANDOS
    // ==========================

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        log.info("Criando categoria: {}", dto.getName());

        Category entity = new Category();
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        log.info("Categoria criada com id: {}", entity.getId());
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        log.info("Atualizando categoria id: {}", id);

        Category entity = findEntityById(id);
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        log.info("Deletando categoria id: {}", id);

        if (!repository.existsById(id)) {
            log.error("Categoria não encontrada id: {}", id);
            throw new ResourceNotFoundException("Recurso não encontrado para o id: " + id);
        }

        try {
            repository.deleteById(id);
            log.info("Categoria deletada com sucesso id: {}", id);

        } catch (DataIntegrityViolationException e) {
            log.error("Erro de integridade ao deletar categoria id: {}", id);
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    // ==========================
    // AUXILIARES
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