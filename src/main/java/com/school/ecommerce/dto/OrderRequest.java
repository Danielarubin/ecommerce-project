package com.school.ecommerce.dto;

import java.util.List;

public class OrderRequest {
    private Long buyerId;
    private List<OrderItemRequest> items;

    public OrderRequest() {}

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
