package com.cafe.server.helper.ultis;

import com.cafe.server.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperUtils {
    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static <T> T toObject(String data, Class<T> tClass) {
        try {
            return objectMapper.readValue(data, tClass);
        } catch (IOException err) {
            log.info("Error convert to object class {} with error {}", tClass, err.getMessage());
            throw new GeneralException(err);
        }
    }
}
