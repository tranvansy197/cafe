package com.cafe.server.mapper;

import com.cafe.entity.OrderBill;
import com.cafe.entity.OrderItem;
import com.cafe.model.dto.bill.BillDTO;
import com.cafe.model.dto.bill.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderBillMapper {
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapOrderItem")
    OrderBill toEntity(BillDTO billDTO);

    @Named("mapOrderItem")
    default List<OrderItem> mapOrderItem(List<OrderItemDTO> list) {
        if ( list == null ) {
            return Collections.emptyList();
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemDTO orderItemDTO : list ) {
            list1.add( orderItemDTOToOrderItem( orderItemDTO ) );
        }

        return list1;
    }

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "store.id", source = "storeId")
    @Mapping(target = "order.id", source = "orderId")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);
}
