package shop.ecoswapshop.domain;

import lombok.Getter;

public enum UserType {
    ADMIN("ADMIN"),
    USER("USER");

    @Getter
    private final String role;

    UserType(String role) {
        this.role = role;
    }
}
