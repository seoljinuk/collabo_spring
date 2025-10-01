package com.coffee.service;

import com.coffee.entity.Order;
import com.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository ;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findByMemberId(Long memberId) {
        return orderRepository.findByMemberIdOrderByIdDesc(memberId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByIdDesc() ;
    }
}
