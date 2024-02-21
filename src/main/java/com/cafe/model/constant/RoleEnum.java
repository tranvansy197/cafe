package com.cafe.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("admin"),
    EMPLOYEE("employee"),
    CUSTOMER("customer");
    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
