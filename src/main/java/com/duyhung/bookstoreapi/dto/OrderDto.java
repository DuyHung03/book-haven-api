package com.duyhung.bookstoreapi.dto;

import com.duyhung.bookstoreapi.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderId;
    private Date orderDate;
    private String totalAmount;
    private OrderStatus orderStatus;
    private String shippingCode;
    private List<OrderItemDto> orderItems;
}
