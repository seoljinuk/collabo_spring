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

    public Optional<CartProduct> findCartProductById(Long cartProductId) {
        return cartProductRepository.findById(cartProductId);
    }
}
