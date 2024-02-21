package com.cafe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;
    private String address;
    @Column(columnDefinition = "varchar(100)", unique = true)
    private String email;
    private String name;
    @Column(columnDefinition = "varchar(100)", unique = true)
    private String phoneNumber;
}
