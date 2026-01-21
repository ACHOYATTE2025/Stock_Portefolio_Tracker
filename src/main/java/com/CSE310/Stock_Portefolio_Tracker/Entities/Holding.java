package com.CSE310.Stock_Portefolio_Tracker.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    // Relation avec Portfolio
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portefolio portfolio;

    // Relation avec Stock
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;


}
