package com.webflux.demo.payloads.clients.FakeAPI;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;
    private Address address;
    private String __v;

    @Data
    public static class Name {
        private String firstname;
        private String lastname;
    }

    @Data
    public static class Address {
        private String city;
        private String street;
        private Integer number;
        private String zipcode;
        private Geo geo;

        @Data
        public static class Geo {
            private String lat;
            private String lng;
        }
    }
}