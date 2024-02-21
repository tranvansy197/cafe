package com.cafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(columnDefinition = "datetime")
    private Date createDate;
    @Column(columnDefinition = "datetime")
    private Date updateDate;
    @Column(columnDefinition = "varchar(45)",unique = true)
    private String username;
    @Column(columnDefinition = "varchar(45)")
    private String fullName;
    @JsonIgnore
    private String password;
    @Column(columnDefinition = "varchar(45)",unique = true)
    private String email;
    @Column(columnDefinition = "varchar(20)")
    private String otp;
    @Column(columnDefinition = "varchar(20)",unique = true)
    private String phoneNumber;
    @Column(columnDefinition = "varchar(255)")
    private String address;
    @Column(columnDefinition = "varchar(255)")
    private String imageUrl;
    @Column(columnDefinition = "BIT")
    private Boolean isConfirmed;
    @Column(columnDefinition = "BIT")
    private Boolean isLocked;
    @Column(columnDefinition = "varchar(100)")
    private String refreshToken;
    @Column(columnDefinition = "datetime")
    private LocalDateTime refreshTokenExpire;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private Store store;

}
