package com.f3pro.dscatalog.resources;


import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {


    private final CategoryService service;

    @Autowired
    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar todas as categorias")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        var categories = service.findAll();
        return ResponseEntity.ok().body(categories);

    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping( value ="/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        var dto = service.findById(id);
        return ResponseEntity.ok().body(dto);

    }
    @Operation(summary = "Criar uma nova categoria")
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
        dto = service.insert(dto);
        var uri= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);

    }
    @Operation(summary = "Atualizar uma nova categoria por ID")
   @PutMapping( value ="/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        dto =service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }


    @Operation(summary = "Deletar uma nova categoria por ID")
    @DeleteMapping( value ="/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long id) {
      service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
