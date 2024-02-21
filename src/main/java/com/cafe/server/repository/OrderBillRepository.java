package com.cafe.server.repository;

import com.cafe.entity.OrderBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBillRepository extends JpaRepository<OrderBill, Long> {
}
