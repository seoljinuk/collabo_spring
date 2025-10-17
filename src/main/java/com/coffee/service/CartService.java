package com.coffee.service;

import com.coffee.dto.CartItemDto;
import com.coffee.dto.CartProductDto;
import com.coffee.entity.Cart;
import com.coffee.entity.CartProduct;
import com.coffee.entity.Member;
import com.coffee.entity.Product;
import com.coffee.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    // 카트에 상품 담기 로직
    private final CartRepository cartRepository ;
    private final MemberService memberService ;
    private final ProductService productService ;
    private final CartProductService cartProductService ;

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    private CartProduct findExistingProduct(Cart cart, Product product) {
        if (cart.getCartProducts() == null) return null;

        for (CartProduct cp : cart.getCartProducts()) {
            if (cp.getProduct().getId().equals(product.getId())) {
                return cp;
            }
        }
        return null;
    }

    @Transactional
    public String addProductToCart(CartProductDto dto) {
        // 1. 회원 및 상품 검증
        Optional<Member> memberOptional = memberService.findMemberById(dto.getMemberId());
        Optional<Product> productOptional = productService.findProductById(dto.getProductId());

        if (memberOptional.isEmpty() || productOptional.isEmpty()) {
            throw new IllegalArgumentException("회원 또는 상품 정보가 올바르지 않습니다.");
        }

        Member member = memberOptional.get();
        Product product = productOptional.get();

        // 2. 재고 확인
        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }

        // 3. Cart 조회 또는 생성
        Cart cart = cartRepository.findByMember(member).orElse(null);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMember(member);
            cart = saveCart(newCart);
        }

        // 4. 기존 상품 있는지 확인 후 수량 처리
        CartProduct existingCartProduct = findExistingProduct(cart, product);
        if (existingCartProduct != null) {
            existingCartProduct.setQuantity(existingCartProduct.getQuantity() + dto.getQuantity());
            cartProductService.saveCartProduct(existingCartProduct);
        } else {
            CartProduct cp = new CartProduct();
            cp.setCart(cart);
            cp.setProduct(product);
            cp.setQuantity(dto.getQuantity());
            cartProductService.saveCartProduct(cp);
        }

        return "요청하신 상품이 장바구니에 추가되었습니다.";
    }

    /* 특정 회원의 카트 상품 목록 조회 */
    public List<CartItemDto> getCartItemsByMemberId(Long memberId) {
        // 1. 회원 조회
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));

        // 2. 회원의 카트 조회
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(Cart::new); // 없으면 빈 카트 생성

        // 3. CartProduct → CartItemDto 변환
        return cart.getCartProducts().stream()
                .map(CartItemDto::new)
                .toList(); // Java 16 이상, 이전 버전은 collect(Collectors.toList())
    }
}
