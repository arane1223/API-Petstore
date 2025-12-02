package data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PetStatus {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    private final String value;

    PetStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static String[] getAllValues() {
        return Arrays.stream(values())
                .map(PetStatus::getValue)
                .toArray(String[]::new);
    }
}