package com.webflux.demo.payloads.clients.FakeAPI;

import lombok.Data;

import java.util.Map;

@Data
public class Cart {
    private Integer id;
    private Integer userId;
    private Integer date;
    private Map<Integer, CartProduct> products;

    @Data
    public static class CartProduct {
        private Integer productId;
        private Integer quantity;
    }
}