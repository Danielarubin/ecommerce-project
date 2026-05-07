package com.school.ecommerce.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private Role role;

    // Seller fields
    private String location;
    
    @Column(name = "avatar_initial", length = 2)
    private String avatarInitial;
    
    @Column(name = "avatar_gradient")
    private String avatarGradient;
    
    @Column(name = "main_bg")
    private String mainBg;
    
    @Column(name = "sub1_bg")
    private String sub1Bg;
    
    @Column(name = "sub2_bg")
    private String sub2Bg;

    @ElementCollection
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    // Relationships
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCollection> collections = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrder> orders = new ArrayList<>();

    public User() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAvatarInitial() { return avatarInitial; }
    public void setAvatarInitial(String avatarInitial) { this.avatarInitial = avatarInitial; }
    public String getAvatarGradient() { return avatarGradient; }
    public void setAvatarGradient(String avatarGradient) { this.avatarGradient = avatarGradient; }
    public String getMainBg() { return mainBg; }
    public void setMainBg(String mainBg) { this.mainBg = mainBg; }
    public String getSub1Bg() { return sub1Bg; }
    public void setSub1Bg(String sub1Bg) { this.sub1Bg = sub1Bg; }
    public String getSub2Bg() { return sub2Bg; }
    public void setSub2Bg(String sub2Bg) { this.sub2Bg = sub2Bg; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    public List<ProductCollection> getCollections() { return collections; }
    public void setCollections(List<ProductCollection> collections) { this.collections = collections; }
    public List<PurchaseOrder> getOrders() { return orders; }
    public void setOrders(List<PurchaseOrder> orders) { this.orders = orders; }
}
