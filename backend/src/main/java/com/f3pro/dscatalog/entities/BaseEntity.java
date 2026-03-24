package com.f3pro.dscatalog.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

/*    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant deletedAt;*/

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }


}