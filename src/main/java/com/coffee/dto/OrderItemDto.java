package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 주문이 발생할 때 1건의 상품 정보를 저장하고 있는 클래스
@Getter @Setter @ToString
public class OrderItemDto {
    // 변수 cartProductId는 '카트 목록 보기(CartList.)' 메뉴에서만 사용이 됩니다.
    private Long cartProductId ; // 카트 상품 번호
    private Long productId ; // 상품 번호
    private int quantity ; // 구매 수량
}
