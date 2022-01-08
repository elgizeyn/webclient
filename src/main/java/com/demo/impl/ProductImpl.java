package com.demo.impl;

import com.demo.entity.Product;
import com.demo.interfaces.ProductInterface;
import com.demo.repo.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

public class ProductImpl {
    @Service
    public static class ProImpl implements ProductInterface {

        private final ProductRepository productRepository;

        public ProImpl(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @Override
        public Product getbyName(String name) {
            return productRepository.getByName(name);
        }

        @Override
        public void insert(Product product) {
            productRepository.save(product);
        }

        @Override
        public List<Product> getList() {
            return productRepository.findAll();
        }
    }
}
