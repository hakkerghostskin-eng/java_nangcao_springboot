package com.example.demoaaaaa.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartTopping implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String name;
    private final BigDecimal price;

    public CartTopping(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
