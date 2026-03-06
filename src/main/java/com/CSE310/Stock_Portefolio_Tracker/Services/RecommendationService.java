package com.CSE310.Stock_Portefolio_Tracker.Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Holding;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Recommendation;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Stock;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final StockApiClient stockApiClient;

  

    public Recommendation generateRecommendation(Holding holding) {
        Stock stock = holding.getStock();
        BigDecimal buyPrice = holding.getAmount(); // prix moyen ou montant investi

        // Appel de l'API pour récupérer le prix actuel
        BigDecimal currentPrice = BigDecimal.ZERO;
        try {
            GlobalQuoteResponse response = stockApiClient.getStockPrice(stock.getSymbol());
            if (response != null && response.getQuote() != null && response.getQuote().getPrice() != null) {
                currentPrice = new BigDecimal(response.getQuote().getPrice());
            }
        } catch (Exception e) {
            // Log l'erreur mais on continue avec BigDecimal.ZERO pour éviter NPE
            System.err.println("Erreur lors de la récupération du prix pour " + stock.getSymbol() + ": " + e.getMessage());
        }

        stock.setCurrentPrice(currentPrice); // optionnel, si tu veux garder la valeur dans l'entité

        Recommendation recommendation = new Recommendation();
        recommendation.setStock(stock);
        recommendation.setDate(LocalDateTime.now());

        // si currentPrice est zéro, on ne fait pas le calcul
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            recommendation.setAdvice("HOLD");
            recommendation.setComment("Prix actuel indisponible");
            return recommendation;
        }

        BigDecimal change = currentPrice.subtract(buyPrice)
                .divide(buyPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (change.compareTo(BigDecimal.valueOf(10)) > 0) {
            recommendation.setAdvice("SELL");
            recommendation.setComment("Profit supérieur à 10%");
        } else if (change.compareTo(BigDecimal.valueOf(-10)) < 0) {
            recommendation.setAdvice("BUY");
            recommendation.setComment("Prix bas intéressant");
        } else {
            recommendation.setAdvice("HOLD");
            recommendation.setComment("Conserver l'action");
        }

        return recommendation;
    }
}