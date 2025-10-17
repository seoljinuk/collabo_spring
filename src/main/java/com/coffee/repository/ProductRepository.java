package com.coffee.repository;

import com.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여 주어야 합니다.
    List<Product> findProductByOrderByIdDesc();
}
