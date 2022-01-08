package com.demo.interfaces;

import com.demo.entity.Product;
import java.util.List;

public interface ProductInterface {
    Product getbyName(String name);
    void insert(Product product);
    List<Product> getList();
}





