package com.cafe.server.service.impl;

import com.cafe.model.dto.bill.BillDTO;
import com.cafe.server.mapper.OrderBillMapper;
import com.cafe.server.repository.OrderBillRepository;
import com.cafe.server.service.OrderBillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderBillServiceImpl implements OrderBillService {
    private OrderBillRepository orderBillRepository;
    private OrderBillMapper orderBillMapper;

    @Override
    @Transactional
    public void createBill(BillDTO billDTO) {
        orderBillRepository.save(orderBillMapper.toEntity(billDTO));
    }
}
