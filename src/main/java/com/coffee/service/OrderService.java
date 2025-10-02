package com.coffee.service;

import com.coffee.constant.OrderStatus;
import com.coffee.entity.Order;
import com.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository ;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findByMemberId(Long memberId, OrderStatus status) {
        return orderRepository.findByMemberIdAndStatusOrderByIdDesc(memberId, status);
    }

    public List<Order> findAllOrders(OrderStatus status) {
        return orderRepository.findByStatusOrderByIdDesc(status) ;
    }

    public int updateOrderStatus(Long orderId, OrderStatus status) {
        return orderRepository.updateOrderStatus(orderId, status);
    }

    public boolean existsById(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);

    }

    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
