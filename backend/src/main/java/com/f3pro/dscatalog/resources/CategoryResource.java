package com.f3pro.dscatalog.resources;


import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {


    private final CategoryService service;

    @Autowired
    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        var categories = service.findAll();
        return ResponseEntity.ok().body(categories);

    }


    @GetMapping( value ="/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        var dto = service.findById(id);
        return ResponseEntity.ok().body(dto);

    }
}
