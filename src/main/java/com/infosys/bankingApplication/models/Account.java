package com.infosys.bankingApplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private double balance;

    private boolean lowBalanceAlertSent;

    public Account(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.lowBalanceAlertSent = false;
    }
}
