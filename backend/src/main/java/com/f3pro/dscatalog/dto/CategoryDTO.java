package com.f3pro.dscatalog.dto;

import com.f3pro.dscatalog.entities.Category;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO {


    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

}
