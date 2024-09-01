package shop.olcl.backend.common.exception;

public class OrdersNotFoundException extends RuntimeException {

    public OrdersNotFoundException(String message) {
        super(message);
    }

    public OrdersNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
