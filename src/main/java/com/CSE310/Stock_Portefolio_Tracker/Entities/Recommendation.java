package com.CSE310.Stock_Portefolio_Tracker.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String advice; // ex: "BUY", "SELL", "HOLD"
    private String comment;
    private LocalDateTime date;

    // Relation avec Stock
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

}
