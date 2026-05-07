package com.school.ecommerce.dto;

public class CartItemDto {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    private String selectedSize;
    private Integer qty;

    public CartItemDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}
