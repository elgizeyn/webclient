package com.demo.controller;

import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.interfaces.ProductInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

// Get list of JSON objects with RestTemplate | WebClient | Mono | Flux
@RestController
public class ProductController {

    private final ProductInterface productInterface;
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public ProductController(ProductInterface productInterface, RestTemplate restTemplate, WebClient webClient) {
        this.productInterface = productInterface;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @PostMapping("/add/{name}")
    public ResponseEntity<Product> add(@PathVariable String name){
        Product product = new Product();
        product.setName(name);
        productInterface.insert(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productInterface.getList();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{name}")
    public ResponseEntity<Product> getProduct(@PathVariable String name){
        Product product = productInterface.getbyName(name);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/mono/{name}")
    public ResponseEntity<Mono<Product>> getByMono(@PathVariable String name) {
        Mono<Product> productMono = webClient
                .get().uri("/products/" + name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class);
        return new ResponseEntity<>(productMono, HttpStatus.OK);
    }

    @GetMapping("/flux")
    public ResponseEntity<List<Product>> getByFlux() {
        Flux<Product> productFlux = webClient
                .get().uri("/products")
                .retrieve()
                .bodyToFlux(Product.class);
        List<Product> productList = productFlux.collect(Collectors.toList()).share().block();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/restTemplate")
    public ResponseEntity<?> getByRestTemplate() {
        String url = "http://localhost:8080/products";
        ResponseEntity<?> product = restTemplate.getForEntity(url, Product[].class);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/restTemplate1")
    public ResponseEntity<?> getUser() throws URISyntaxException {
        URI url = new URI("https://jsonplaceholder.typicode.com/posts/1");
        ResponseEntity<User> user = restTemplate.getForEntity(url, User.class);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
