package com.shepherdmoney.interviewproject.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private LocalDate date;

    private double balance;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnoreProperties("balanceHistory")
    private CreditCard creditCard;
}
