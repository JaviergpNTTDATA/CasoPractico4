package com.novabank.account.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iban", unique = true, nullable = false)
    private String iban;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Movement> movements = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    public Account(Long clientId) {
        this.clientId = clientId;
    }

    @PrePersist
    public void prePersist() {
        if (balance == null) balance = BigDecimal.ZERO;
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public void deposit(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid balance");
        }
        this.balance = this.balance.add(balance);
    }

    public void withdraw(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid balance");
        }
        if (this.balance.compareTo(balance) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance = this.balance.subtract(balance);
    }

}
