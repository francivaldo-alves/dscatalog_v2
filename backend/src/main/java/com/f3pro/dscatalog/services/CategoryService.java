package com.f3pro.dscatalog.services;


import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll().
                stream().map(CategoryDTO::new).collect(Collectors.toList());

    }
}
