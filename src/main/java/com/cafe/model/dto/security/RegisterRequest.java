package com.cafe.model.dto.security;

import com.cafe.model.constant.RoleEnum;
import com.cafe.server.helper.annotation.EnumListPattern;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String fullName;
    @Size(min = 5,max = 20, message = "Username must be greater than 3 characters and less than 20 characters.")
    @Pattern(regexp = "\\w+", message = "Username Not in correct format. Only enter letters or numbers.")
    private String username;
    private String email;
    private String password;
    @EnumListPattern(enumClass = RoleEnum.class, message = "Invalid value for roles. Allowed values are admin, employee, customer")
    private Set<String> roles;
}
