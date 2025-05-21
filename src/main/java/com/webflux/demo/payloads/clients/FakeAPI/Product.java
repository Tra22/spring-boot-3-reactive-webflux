package com.webflux.demo.payloads.clients.FakeAPI;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    @Data
    public static class Rating {
        private Double rate;
        private Integer count;
    }
}