package com.f3pro.dscatalog.services;

import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import com.f3pro.dscatalog.services.exceptions.DatabaseException;
import com.f3pro.dscatalog.services.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j // Habilita logs com SLF4J (Lombok)
@Service // Define como componente de serviço do Spring
public class CategoryService {

    private final CategoryRepository repository;

    // Injeção de dependência via construtor (boa prática)
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

   /* *//**
     * Busca todas as categorias cadastradas
     * Transação somente leitura para melhor performance
     *//*
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();
    }*/


    /**
     * Busca todas as categorias cadastradas
     * Transação somente leitura para melhor performance
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(CategoryDTO::new);
    }


    /**
     * Busca uma categoria por ID
     * Lança exceção caso não encontre
     */
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(findEntityById(id));
    }

    /**
     * Insere uma nova categoria no banco
     */
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        log.info("Criando uma categoria: {}", dto.getName());

        Category entity = new Category();
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        log.info("Categoria criada com id: {}", entity.getId());
        return new CategoryDTO(entity);
    }

    /**
     * Atualiza uma categoria existente
     * Valida existência antes de atualizar
     */
   @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        log.info("Atualizando categoria id: {}", id);

        Category entity = findEntityById(id);
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new CategoryDTO(entity);
    }
   /* @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        log.info("Atualizando categoria id: {}", id);
        try {
            Category entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityActionVetoException e) {
            log.error("Erro ao atualizar - id não encontrado: {}", id);
            throw new ResourceNotFoundException("Categoria não encontrada para o id " + id);
        }

    }*/


    /**
     * Remove uma categoria pelo ID
     * - Valida se o ID existe antes de deletar
     * - Trata erro de integridade referencial
     * <p>
     * Propagation.SUPPORTS:
     * Executa dentro de uma transação existente ou sem transação
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        log.info("Deletando categoria id: {}", id);

        // Validação prévia para garantir que o recurso existe
        if (!repository.existsById(id)) {
            log.info("Categoria inativada com sucesso id: {}", id);
            throw new ResourceNotFoundException("Recurso não encontrado para o id: " + id);
        }

        try {
            repository.deleteById(id);
            log.info("Categoria deletada com sucesso id: {}", id);

        } catch (DataIntegrityViolationException e) {
            // Ex: categoria vinculada a outro registro (FK)
            log.error("Erro de integridade ao deletar categoria id: {}", id);
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================

    /**
     * Busca entidade por ID ou lança exceção
     */
    private Category findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada para o id " + id)
                );
    }


    /**
     * Copia dados do DTO para a entidade
     */
    private void copyDtoToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.getName());
    }
}