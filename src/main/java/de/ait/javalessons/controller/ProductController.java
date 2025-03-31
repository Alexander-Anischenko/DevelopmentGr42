package de.ait.javalessons.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/public/list")
    public ResponseEntity<String> getPproducts() {
        return ResponseEntity.ok("Product List, public user information");
    }

    @GetMapping("/customer/cart")
    public ResponseEntity<String> getCart() {
        return ResponseEntity.ok("Cart of products, customer information");
    }

    @GetMapping("/manager/add")
    public ResponseEntity<String> addProduct() {
        return ResponseEntity.ok("Add product, manager information");
    }

}
