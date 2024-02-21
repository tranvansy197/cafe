package com.cafe.server.helper.ultis;

import com.cafe.model.core.OffsetLimitPageable;
import com.cafe.model.core.PaginationDTO;
import com.cafe.model.core.SortDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    public static PaginationDTO createPagination(String strPagination) {
        return !StringUtils.isEmpty(strPagination) ?
                ObjectMapperUtils.toObject(strPagination, PaginationDTO.class) :
                new PaginationDTO();
    }

    public static SortDTO createSort(String strSort) {
        return !StringUtils.isEmpty(strSort) ?
                ObjectMapperUtils.toObject(strSort, SortDTO.class) :
                new SortDTO("id" , 1);
    }

    public static SortDTO createAndUpdateFieldSortIfDifferent(String strSort, @Nonnull Map<String, String> customFieldMapping) {
        SortDTO sort = createSort(strSort);

        if (customFieldMapping.containsKey(sort.getField())) {
            sort.setField(customFieldMapping.get(sort.getField()));
        }
        return sort;
    }

    public static OffsetLimitPageable createOffsetLimitPageable(PaginationDTO paginationDTO, SortDTO sortDTO) {
        var order = sortDTO.getOrder() >= 0 ?
                new Sort.Order(org.springframework.data.domain.Sort.Direction.ASC, sortDTO.getField()) :
                new Sort.Order(org.springframework.data.domain.Sort.Direction.DESC, sortDTO.getField());
        return new OffsetLimitPageable(paginationDTO.getOffset(), paginationDTO.getLimit(), org.springframework.data.domain.Sort.by(order));
    }
}
