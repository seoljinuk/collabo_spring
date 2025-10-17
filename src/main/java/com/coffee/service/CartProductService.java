package com.coffee.service;

import com.coffee.entity.CartProduct;
import com.coffee.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository ;

    public void saveCartProduct(CartProduct cp) {
        this.cartProductRepository.save(cp);
    }

    public String editCartProductQuantity(Long cartProductId, Integer quantity) {
        // 1. 수량 검증
        if (quantity == null || quantity < 1) {
            return "오류: 장바구니 품목은 최소 1개 이상이어야 합니다.";
        }

        // 2. 해당 상품 찾기
        Optional<CartProduct> cartProductOptional = cartProductRepository.findById(cartProductId);
        if (cartProductOptional.isEmpty()) {
            return "오류: 장바구니 품목을 찾을 수 없습니다.";
        }

        // 3. 수량 변경
        CartProduct cartProduct = cartProductOptional.get();
        cartProduct.setQuantity(quantity);
        // cartProduct.setQuantity(cartProduct.getQuantity() + quantity); // 누적 변경 시

        // 4. DB 저장
        cartProductRepository.save(cartProduct);

        // 5. 성공 메시지 반환
        return "카트 상품 아이디 " + cartProductId + "번이 `" + quantity + "개`로 수정이 되었습니다.";
    }

    public void deleteCartProductById(Long cartProductId) {
        cartProductRepository.deleteById(cartProductId);
    }
}
