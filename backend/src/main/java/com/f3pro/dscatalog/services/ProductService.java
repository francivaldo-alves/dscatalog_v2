package com.f3pro.dscatalog.services;

import com.f3pro.dscatalog.dto.CategoryDTO;
import com.f3pro.dscatalog.dto.ProductDTO;
import com.f3pro.dscatalog.entities.Category;
import com.f3pro.dscatalog.entities.Product;
import com.f3pro.dscatalog.repositories.CategoryRepository;
import com.f3pro.dscatalog.repositories.ProductRepository;
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
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return new ProductDTO(findEntityById(id));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        log.info("Criando produto: {}", dto.getName());

        Product entity = new Product();
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        log.info("Produto criado com id: {}", entity.getId());
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        log.info("Atualizando produto id: {}", id);

        Product entity = findEntityById(id);
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        log.info("Deletando produto id: {}", id);

        if (!repository.existsById(id)) {
            log.error("Produto não encontrado id: {}", id);
            throw new ResourceNotFoundException("Recurso não encontrado para o id: " + id);
        }

        try {
            repository.deleteById(id);
            log.info("Produto deletado com sucesso id: {}", id);

        } catch (DataIntegrityViolationException e) {
            log.error("Erro de integridade ao deletar produto id: {}", id);
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    // ==========================
    // AUXILIARES
    // ==========================

    private Product findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produto não encontrado para o id " + id)
                );
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());

        entity.getCategories().clear();
      dto.getCategories().forEach(categoryDTO -> {
          Category category = categoryRepository.getReferenceById(categoryDTO.getId());
          entity.getCategories().add(category);
      });

    }
}