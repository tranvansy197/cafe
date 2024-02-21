package com.cafe.server.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class MethodArgumentExceptionResponse {
    private Integer statusCode;
    private List<ErrorField> errorFields;
    private Date timestamp;
}
