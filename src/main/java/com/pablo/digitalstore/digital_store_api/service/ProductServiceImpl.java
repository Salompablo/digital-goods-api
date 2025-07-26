package com.pablo.digitalstore.digital_store_api.service;

import com.pablo.digitalstore.digital_store_api.exception.ProductAlreadyExistsException;
import com.pablo.digitalstore.digital_store_api.model.dto.request.ChangePasswordRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.ProductRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.entity.ProductEntity;
import com.pablo.digitalstore.digital_store_api.model.mapper.ProductMapper;
import com.pablo.digitalstore.digital_store_api.repository.CredentialsRepository;
import com.pablo.digitalstore.digital_store_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        if (productRepository.existsByName(productRequest.getName())) {
            throw new ProductAlreadyExistsException("Product with name " + productRequest.getName() + " already exists.");
        }

        ProductEntity productEntity = productMapper.toEntity(productRequest);
        productEntity = productRepository.save(productEntity);

        return productMapper.toResponse(productEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product with id " + id + " not found.");
        }

        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findProductById(Long id){
        return productMapper.toResponse(productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with id " + id + " not found.")));
    }

    @Override
    public Page<ProductResponse> findAllProducts(Pageable pageable) {
        return productMapper.toResponsePage(productRepository.findAll(pageable));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found."));

        productEntity.setName(productRequest.getName());
        productEntity.setDescription(productRequest.getDescription());
        productEntity.setPrice(productRequest.getPrice());
        productEntity.setType(productRequest.getType());
        productEntity.setPremiumOnly(productRequest.getPremiumOnly());
        productEntity.setFileUrl(productRequest.getFileUrl());

        productEntity = productRepository.save(productEntity);

        return productMapper.toResponse(productEntity);
    }
}
