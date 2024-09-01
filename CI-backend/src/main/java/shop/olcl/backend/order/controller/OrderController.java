package shop.olcl.backend.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.olcl.backend.common.exception.OrdersNotFoundException;
import shop.olcl.backend.common.exception.UserNotFoundException;
import shop.olcl.backend.order.service.OrderService;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal Jwt principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cognitoUserId = authentication.getName();
        try {
            return ResponseEntity.ok().body(orderService.getOrders(cognitoUserId));
        } catch (OrdersNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
