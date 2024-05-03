package shop.ecoswapshop.domain;

import lombok.Getter;

public enum UserType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    @Getter
    private final String role;

    UserType(String role) {
        this.role = role;
    }
}
