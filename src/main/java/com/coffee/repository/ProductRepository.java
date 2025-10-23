package com.coffee.repository;

import com.coffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여 주어야 합니다.
    List<Product> findProductByOrderByIdDesc();

    // 검색 조건인 spec와 페이징 객체 pageable를 사용하여 데이터를 검색합니다.
    // 정렬 방식은 pageable 객체에 포함되어 있습니다.
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
