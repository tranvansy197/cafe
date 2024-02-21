package com.cafe.server.service;

import com.cafe.model.dto.bill.BillDTO;

public interface OrderBillService {
    void createBill(BillDTO billDTO);
}
