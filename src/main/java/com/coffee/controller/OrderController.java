package com.coffee.controller;

import com.coffee.dto.OrderDto;
import com.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService ;

    @PostMapping("") // CartList.js 파일의 makeOrder() 함수와 연관이 있습니다.
    public ResponseEntity<?> order(@RequestBody OrderDto dto){
        System.out.println(dto);
        return null ;
    }
}
