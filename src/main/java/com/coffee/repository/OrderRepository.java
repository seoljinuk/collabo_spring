package com.coffee.repository;

import com.coffee.constant.OrderStatus;
import com.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 쿼리 메소드를 사용하여 특정 회원의 송장 번호가 큰 것(최신 주문) 것부터 조회합니다.
    // 주문의 상태가 PENDING인것만 조회합니다.
    // cf. 좀더 복잡한 쿼리를 사용하시려면 @Query 또는 querydsl을 사용하세요.
    List<Order> findByMemberIdAndStatusOrderByIdDesc(Long memberId, OrderStatus status);

    // 주문 번호(id) 기준으로 모든 주문 내역을 역순(내림차순) 으로 조회하려면 JPA 메서드를 이렇게 작성하시면 됩니다.
    // 주문의 상태가 PENDING인것만 조회합니다.
    List<Order> findByStatusOrderByIdDesc(OrderStatus status); // 이건 관리자가 사용합니다.
}
