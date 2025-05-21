package com.webflux.demo.controllers;

import com.webflux.demo.payloads.clients.ApiResponse;
import com.webflux.demo.payloads.clients.FakeAPI.Cart;
import com.webflux.demo.payloads.clients.FakeAPI.LoginRequest;
import com.webflux.demo.payloads.clients.FakeAPI.Product;
import com.webflux.demo.payloads.clients.FakeAPI.User;
import com.webflux.demo.services.clients.FakeStoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/fake-api")
@RequiredArgsConstructor
public class FakeAPIController {
    private final FakeStoreClient fakeStoreClient;

    // === Products ===
    @GetMapping("/products")
    public Flux<ApiResponse<Product>> getAllProducts() {
        return fakeStoreClient.getAllProducts()
                .map(ApiResponse::ok);
    }

    @GetMapping("/products/{id}")
    public Mono<ApiResponse<Product>> getProductById(@PathVariable int id) {
        return fakeStoreClient.getProductById(id)
                .map(ApiResponse::ok);
    }

    @GetMapping("/products/categories")
    public Flux<ApiResponse<String>> getAllCategories() {
        return fakeStoreClient.getAllCategories()
                .map(ApiResponse::ok);
    }

    @GetMapping("/products/category/{category}")
    public Flux<ApiResponse<Product>> getProductsByCategory(@PathVariable String category) {
        return fakeStoreClient.getProductsByCategory(category)
                .map(ApiResponse::ok);
    }

    @GetMapping("/products/limited")
    public Flux<ApiResponse<Product>> getLimitedProducts(@RequestParam int limit) {
        return fakeStoreClient.getLimitedProducts(limit)
                .map(ApiResponse::ok);
    }

    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Product>> addProduct(@RequestBody Product product) {
        return fakeStoreClient.addProduct(product)
                .map(ApiResponse::ok);
    }

    @PutMapping(value = "/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Product>> updateProduct(@PathVariable int id, @RequestBody Product product) {
        return fakeStoreClient.updateProduct(id, product)
                .map(ApiResponse::ok);
    }

    @DeleteMapping("/products/{id}")
    public Mono<ApiResponse<Void>> deleteProduct(@PathVariable int id) {
        return fakeStoreClient.deleteProduct(id)
                .thenReturn(ApiResponse.ok(null));
    }

    // === Carts ===
    @GetMapping("/carts")
    public Flux<ApiResponse<Cart>> getAllCarts() {
        return fakeStoreClient.getAllCarts()
                .map(ApiResponse::ok);
    }

    @GetMapping("/carts/{id}")
    public Mono<ApiResponse<Cart>> getCartById(@PathVariable int id) {
        return fakeStoreClient.getCartById(id)
                .map(ApiResponse::ok);
    }

    @GetMapping("/carts/user/{userId}")
    public Flux<ApiResponse<Cart>> getCartsByUserId(@PathVariable int userId) {
        return fakeStoreClient.getCartsByUserId(userId)
                .map(ApiResponse::ok);
    }

    @PostMapping(value = "/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Cart>> addCart(@RequestBody Cart cart) {
        return fakeStoreClient.addCart(cart)
                .map(ApiResponse::ok);
    }

    @PutMapping(value = "/carts/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<Cart>> updateCart(@PathVariable int id, @RequestBody Cart cart) {
        return fakeStoreClient.updateCart(id, cart)
                .map(ApiResponse::ok);
    }

    @DeleteMapping("/carts/{id}")
    public Mono<ApiResponse<Void>> deleteCart(@PathVariable int id) {
        return fakeStoreClient.deleteCart(id)
                .thenReturn(ApiResponse.ok(null));
    }

    // === Users ===
    @GetMapping("/users")
    public Flux<ApiResponse<User>> getAllUsers() {
        return fakeStoreClient.getAllUsers()
                .map(ApiResponse::ok);
    }

    @GetMapping("/users/{id}")
    public Mono<ApiResponse<User>> getUserById(@PathVariable int id) {
        return fakeStoreClient.getUserById(id)
                .map(ApiResponse::ok);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<User>> addUser(@RequestBody User user) {
        return fakeStoreClient.addUser(user)
                .map(ApiResponse::ok);
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<User>> updateUser(@PathVariable int id, @RequestBody User user) {
        return fakeStoreClient.updateUser(id, user)
                .map(ApiResponse::ok);
    }

    @DeleteMapping("/users/{id}")
    public Mono<ApiResponse<Void>> deleteUser(@PathVariable int id) {
        return fakeStoreClient.deleteUser(id)
                .thenReturn(ApiResponse.ok(null));
    }

    // === Auth (Login) ===
    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<User>> loginUser(@RequestBody LoginRequest loginRequest) {
        return fakeStoreClient.loginUser(loginRequest.getUsername(), loginRequest.getPassword())
                .map(ApiResponse::ok);
    }
}
