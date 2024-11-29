package com.example.cafemanagement.dto;

public class HashtagDto {
    private final Long id;

    private final String name;

    public HashtagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
