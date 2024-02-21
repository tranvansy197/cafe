package com.cafe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String address;
    @Column(columnDefinition = "varchar(45)" , unique = true)
    private String email;
    private String name;
    @Column(columnDefinition = "varchar(45)" , unique = true)
    private String phoneNumber;
}
