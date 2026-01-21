package com.CSE310.Stock_Portefolio_Tracker.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double price;
    private String type; // "BUY" ou "SELL"
    private LocalDateTime date;

    // Relation avec Stock
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    // Relation avec Portfolio
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portefolio portfolio;


}
