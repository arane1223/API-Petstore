package data;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum OrderStatus {
    PLACED("placed"),
    APPROVED("approved"),
    DELIVERED("delivered");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static String[] getAllValues() {
        return Arrays.stream(values())
                .map(OrderStatus::getValue)
                .toArray(String[]::new);
    }
}
