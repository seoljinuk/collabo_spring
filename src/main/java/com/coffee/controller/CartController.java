package com.coffee.controller;

import com.coffee.dto.CartProductDto;
import com.coffee.entity.Cart;
import com.coffee.entity.CartProduct;
import com.coffee.entity.Member;
import com.coffee.entity.Product;
import com.coffee.service.CartProductService;
import com.coffee.service.CartService;
import com.coffee.service.MemberService;
import com.coffee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    private final MemberService memberService ;
    private final ProductService productService ;
    private final CartService cartService ;
    private final CartProductService cartProductService ;

    @PostMapping("/insert") // 리액트에서 `장바구니` 버튼을 클릭하였습니다.
    public ResponseEntity<String> addToCart(@RequestBody CartProductDto dto){
        // Member 또는 Product이 유효한 정보인지 확인
        Optional<Member> memberOptional = memberService.findMemberById(dto.getMemberId());
        Optional<Product> productOptional = productService.findProductById(dto.getProductId()) ;

        if(memberOptional.isEmpty() || productOptional.isEmpty()){ // 정보가 무효하면
            return ResponseEntity.badRequest().body("회원 또는 상품 정보가 올바르지 않습니다.");
        }

        // Member와 Product의 객체 정보 가져 오기
        Member member = memberOptional.get(); // 진짜 배기 회원 정보
        Product product = productOptional.get();

        // 재고가 충분한지 확인
        if(product.getStock() < dto.getQuantity()){
            return ResponseEntity.badRequest().body("재고 수량이 부족합니다.");
        }

        // Cart 조회 또는 신규 작성
        Cart cart = cartService.findByMember(member);

        if(cart == null){
            Cart newCart = new Cart(); // 새로운 카트
            newCart.setMember(member); // 고객이 카트를 집어듬
            cart = cartService.saveCart(newCart); // 데이터 베이스에 저장
        }

        // 기존에 같은 상품이 있는지 확인
        CartProduct existingCartProduct = null;

        if(cart.getCartProducts() != null) { // 최초로 장바구니에 담을 때 null이 됩니다.
            for (CartProduct cp : cart.getCartProducts()) {
                // 주의) Long 타입은 참조 자료형이르로 == 대신 equals() 메소드를 사용해야 합니다.
                if (cp.getProduct().getId().equals(product.getId())) {
                    existingCartProduct = cp;
                    break;
                }
            }
        }

        if (existingCartProduct != null) { // 기존 상품이면 수량 누적
            existingCartProduct.setQuantity(existingCartProduct.getQuantity() + dto.getQuantity());
            cartProductService.saveCartProduct(existingCartProduct);

        } else { // 새로운 상품이면 새로 추가
            CartProduct cp = new CartProduct();
            cp.setCart(cart);
            cp.setProduct(product);
            cp.setQuantity(dto.getQuantity());
            cartProductService.saveCartProduct(cp);
        }

        // 재고 수량은 차감하지 않습니다.

        return ResponseEntity.ok("요청하신 상품이 장바구니에 추가되었습니다.") ;
    }
}
