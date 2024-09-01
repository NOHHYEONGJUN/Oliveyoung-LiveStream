package shop.olcl.backend.order.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import shop.olcl.backend.common.exception.OrdersNotFoundException;
import shop.olcl.backend.common.exception.UserNotFoundException;
import shop.olcl.backend.order.dto.OrderResponseDto;
import shop.olcl.backend.order.entity.Order;
import shop.olcl.backend.order.repository.OrderRepository;
import shop.olcl.backend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    public List<OrderResponseDto> getOrders(String cognitoUserId) {
        userRepository.findById(UUID.fromString(cognitoUserId))
                .orElseThrow(() -> new UserNotFoundException("User not found: " + cognitoUserId));

        List<Order> orders = orderRepository.findByUserId(UUID.fromString(cognitoUserId));

        if (orders.isEmpty()) {
            throw new OrdersNotFoundException("Orders not found for user: " + cognitoUserId);
        }

        return orders.stream()
                .map(order -> OrderResponseDto.builder()
                        .orderNumber(order.getOrderNumber())
                        .orderStatus(order.getOrderStatus())
                        .items(order.getItems())
                        .totalAmount(order.getTotalAmount())
                        .build()
                ).collect(java.util.stream.Collectors.toList());
    }
}
