package com.cafe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime date;
    @Column(columnDefinition = "bit default 0")
    private Boolean isLock;
    private String method;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
    private OrderBill order;
}
