package com.f3pro.dscatalog.services;


import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import com.f3pro.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll().
                stream().map(CategoryDTO::new).toList();

    }
    @Transactional(readOnly = true)
    public CategoryDTO findById( Long id) {
       return new CategoryDTO(getEntityById(id));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return new CategoryDTO(repository.save(category));
    }

//método de busca desacoplado
    private Category getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada para o id " + id)
                );
    }


}
