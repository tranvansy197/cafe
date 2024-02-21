package com.cafe.model.constant;

import com.cafe.server.exception.InvalidMatchModeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Operator {
    GREATER_THAN_OR_EQUAL("greaterThanOrEqualTo"),
    LESS_THAN_OR_EQUAL("lessThanOrEqualTo"),
    LIKE("like"),
    EQUAL("equals"),
    IS("is"),
    IS_NOT("isNot"),
    NOT_EQUALS("notEquals"),
    IN("in"),
    CONTAINS("contains"),
    STARTS_WITH("startsWith"),
    ENDS_WITH("endsWith"),
    LTE("lte"),//less than or equals
    LT("lt"), //less than
    GT("gt"),//greater than the filter value
    GTE("gte"),//greater than or equals
    DATE_BEFORE("dateBefore"),//date value is before the filter date
    DATE_AFTER("dateAfter"),//date value is after the filter date
    DATE_IS ("dateIs"),//date value is equal the filter date
    DATE_IS_NOT("dateIsNot");//date value is not equal the filter date
    private final String value;

    public static Operator toOperator(String matchMode) {
        return Arrays.stream(Operator.values())
                .filter(operator -> operator.getValue().equalsIgnoreCase(matchMode))
                .findFirst()
                .orElseThrow(()-> new InvalidMatchModeException(matchMode));
    }
}