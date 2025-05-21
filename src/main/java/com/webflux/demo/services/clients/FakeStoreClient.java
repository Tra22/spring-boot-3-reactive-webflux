package com.webflux.demo.services.clients;

import com.webflux.demo.payloads.clients.FakeAPI.Cart;
import com.webflux.demo.payloads.clients.FakeAPI.LoginRequest;
import com.webflux.demo.payloads.clients.FakeAPI.Product;
import com.webflux.demo.payloads.clients.FakeAPI.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FakeStoreClient {
    private final WebClient fakeAPIWebClient;

    public FakeStoreClient(@Qualifier("fakeAPIWebClient") WebClient fakeAPIWebClient) {
        this.fakeAPIWebClient = fakeAPIWebClient;
    }

    // === Products ===
    public Flux<Product> getAllProducts() {
        return fakeAPIWebClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Product> getProductById(int id) {
        return fakeAPIWebClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Flux<String> getAllCategories() {
        return fakeAPIWebClient.get()
                .uri("/products/categories")
                .retrieve()
                .bodyToFlux(String.class);
    }

    public Flux<Product> getProductsByCategory(String category) {
        return fakeAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products/category/{category}")
                        .build(category))
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Flux<Product> getLimitedProducts(int limit) {
        return fakeAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products")
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Product> addProduct(Product product) {
        return fakeAPIWebClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Product> updateProduct(int id, Product product) {
        return fakeAPIWebClient.put()
                .uri("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Void> deleteProduct(int id) {
        return fakeAPIWebClient.delete()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // === Carts ===

    public Flux<Cart> getAllCarts() {
        return fakeAPIWebClient.get()
                .uri("/carts")
                .retrieve()
                .bodyToFlux(Cart.class);
    }

    public Mono<Cart> getCartById(int id) {
        return fakeAPIWebClient.get()
                .uri("/carts/{id}", id)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Flux<Cart> getCartsByUserId(int userId) {
        return fakeAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/carts/user/{userId}")
                        .build(userId))
                .retrieve()
                .bodyToFlux(Cart.class);
    }

    public Mono<Cart> addCart(Cart cart) {
        return fakeAPIWebClient.post()
                .uri("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cart)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Mono<Cart> updateCart(int id, Cart cart) {
        return fakeAPIWebClient.put()
                .uri("/carts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cart)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Mono<Void> deleteCart(int id) {
        return fakeAPIWebClient.delete()
                .uri("/carts/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // === Users ===

    public Flux<User> getAllUsers() {
        return fakeAPIWebClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Mono<User> getUserById(int id) {
        return fakeAPIWebClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<User> addUser(User user) {
        return fakeAPIWebClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<User> updateUser(int id, User user) {
        return fakeAPIWebClient.put()
                .uri("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<Void> deleteUser(int id) {
        return fakeAPIWebClient.delete()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // === Auth (Login) ===
    public Mono<User> loginUser(String username, String password) {
        return fakeAPIWebClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest(username, password))
                .retrieve()
                .bodyToMono(User.class);
    }
}