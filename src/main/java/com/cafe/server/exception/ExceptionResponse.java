package com.cafe.server.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class ExceptionResponse {
    private Integer statusCode;
    private String message;
    private Date timestamp;
}
