package com.coffee.repository;

import com.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    // 상품의 아이디를 역순으로 정렬하여 상품 목록을 보여 주어야 합니다.
    List<Product> findProductByOrderByIdDesc();

    // image 컬럼에 특정 문자열이 포함된 데이터를 조회합니다.
    // 데이터 베이스의 like 키워드와 유사합니다.
    // select * from products where image like '%bigs%';
    List<Product> findByImageContaining(String keyword);
}
