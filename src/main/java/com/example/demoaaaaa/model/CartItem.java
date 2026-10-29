package com.example.demoaaaaa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Integer productId;
    private final String productName;
    private final String image;
    private final Integer sizeId;
    private final String sizeName;
    private final BigDecimal basePrice;
    private final BigDecimal sizeExtra;
    private final List<Integer> toppingIds;
    private final List<CartTopping> toppings;
    private final BigDecimal toppingTotal;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(Integer productId, String productName, String image, Integer sizeId, String sizeName,
            BigDecimal basePrice, BigDecimal sizeExtra, List<CartTopping> toppings, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.basePrice = basePrice;
        this.sizeExtra = sizeExtra;
        this.toppings = List.copyOf(toppings);
        this.toppingIds = this.toppings.stream().map(CartTopping::getId).toList();
        this.toppingTotal = this.toppings.stream()
                .map(CartTopping::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.unitPrice = basePrice.add(sizeExtra).add(toppingTotal);
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImage() {
        return image;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getSizeExtra() {
        return sizeExtra;
    }

    public List<Integer> getToppingIds() {
        return toppingIds;
    }

    public List<CartTopping> getToppings() {
        return toppings;
    }

    public BigDecimal getToppingTotal() {
        return toppingTotal;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
