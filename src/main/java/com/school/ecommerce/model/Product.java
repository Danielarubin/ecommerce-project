package com.school.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
<<<<<<< HEAD
<<<<<<< HEAD
    private String id;
=======
=======
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
<<<<<<< HEAD
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
=======
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
    private String name;

    @Column(nullable = false, length = 100)
    private String brand;
<<<<<<< HEAD
<<<<<<< HEAD
    private String price;
    private String description;
    private String image; // optional

    public Product() {}

    public Product(String id, String name, String brand, String price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public Product(String id, String name, String brand, String price, String description, String image) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
=======

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(length = 255)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    public Product() {}

=======

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(length = 255)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    public Product() {}

>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
<<<<<<< HEAD
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
=======
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
}
