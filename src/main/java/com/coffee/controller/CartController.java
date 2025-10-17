package com.coffee.controller;

import com.coffee.dto.CartItemDto;
import com.coffee.dto.CartProductDto;
import com.coffee.service.CartProductService;
import com.coffee.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
테스트 시나리오
장바구니가 없을 때 상품을 담기
    1. 로그인한 사람의 회원 아이디와 상품 엔터티의 상품 번호를 확인합니다.
    2. Cart 엔터티의 회원 아이디가 로그인한 사람인가요?
    3. Cart 엔터티의 카트 아이디와 CartProduct 엔터티의 카트 아이디가 동일해야 합니다.
    4. CartProduct 엔터티의 상품 아이디와 Product 엔터티의 상품 아이디가 동일해야 합니다.

장바구니가 있을 때 상품을 담기
    1. Cart 엔터티에 변동 사항은 없습니다.
    2. CartProduct 엔터티에 신규 상품 정보만 추가됩니다.
*/

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor // final 키워드를 가지고 있는 필드에 생성자를 이용하여 자동으로 주입해 줍니다.
public class CartController {
    private final CartService cartService ;


    @PostMapping("/insert")
    public ResponseEntity<String> addToCart(@RequestBody CartProductDto dto) {
        String message = cartService.addProductToCart(dto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/list/{memberId}")// 특정 사용자의 `카트 상품` 목록을 조회합니다.
    public ResponseEntity<List<CartItemDto>> getCartProducts(@PathVariable Long memberId) {
        try {
            List<CartItemDto> cartProducts = cartService.getCartItemsByMemberId(memberId);
            System.out.println("카트 상품 개수 : " + cartProducts.size());
            return ResponseEntity.ok(cartProducts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // 장바구니 내의 특정 상품 수량 변경
    private final CartProductService cartProductService;

    @PatchMapping("/edit/{cartProductId}")
    public ResponseEntity<String> editCartProductQuantity(
            @PathVariable Long cartProductId,
            @RequestParam(required = false) Integer quantity) {

        System.out.println("카트 상품 아이디 : " + cartProductId);
        System.out.println("변경할 갯수 : " + quantity);

        String message = cartProductService.editCartProductQuantity(cartProductId, quantity);

        if (message.startsWith("오류:")) {
            return ResponseEntity.badRequest().body(message);
        }

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{cartProductId}")
    public ResponseEntity<String> deleteCartProduct(@PathVariable Long cartProductId){
        System.out.println("삭제할 카트 상품 아이디 : " + cartProductId);

        cartProductService.deleteCartProductById(cartProductId);

        String message = "카트 상품 " + cartProductId + "번이 장바구니 목록에서 삭제 되었습니다.";
        return ResponseEntity.ok(message) ;
    }
}
