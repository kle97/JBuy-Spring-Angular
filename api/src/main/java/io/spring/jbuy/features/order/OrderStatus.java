package io.spring.jbuy.features.order;

public enum OrderStatus {
//    IN_CART("In Cart"),
    PENDING_PAYMENT("Pending Payment"),
    PROCESSING("Processing"),
    COMPLETE("Complete");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String status() {
        return this.status;
    }
}
