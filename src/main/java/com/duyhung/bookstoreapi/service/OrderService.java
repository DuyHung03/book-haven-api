package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.OrderDto;
import com.duyhung.bookstoreapi.dto.OrderItemDto;
import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;

    public OrderDto saveOrder(OrderDto orderDto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setTotalAmount(orderDto.getTotalAmount());
        orderRepository.save(order);

        List<OrderItem> orderItems = orderDto.getOrderItems().stream().map(orderItemDto -> {
            Book book = bookRepository.findById(orderItemDto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            Inventory inventory = inventoryRepository.findByBookBookId(book.getBookId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            inventory.setStock(inventory.getStock() - orderItemDto.getQuantity());
            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(book.getPrice());
            cartService.deleteItemFromCart(orderItemDto.getBookId(), userId);
            return orderItemRepository.save(orderItem);
        }).toList();

        order.setOrderItems(orderItems);
        return convertToDto(order);
    }

    public List<OrderDto> getOrderByUser(String userId) {
        List<Order> findOrder = orderRepository.findByUserUserId(userId);
        if (findOrder == null) {
            return new ArrayList<>();
        }
        List<OrderDto> list = new ArrayList<>();

        findOrder.forEach(order -> {
            list.add(convertToDto(order));
        });

        return list;
    }

    public OrderItemDto convertToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setBookId(orderItem.getBook().getBookId());
        orderItemDto.setTitle(orderItem.getBook().getTitle());
        orderItemDto.setAuthorName(orderItem.getBook().getAuthor().getAuthorName());
        orderItemDto.setPrice(orderItem.getBook().getPrice());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setImgUrls(orderItem.getBook().getImages().stream().map(BookImage::getImageUrl).toList());
        return orderItemDto;
    }

    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(new Date());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderItems(
                order.getOrderItems().stream().map(this::convertToDto)
                        .collect(Collectors.toList())
        );
        return orderDto;
    }

}
