package com.CSE310.Stock_Portefolio_Tracker.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.HoldingResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.PortfolioResponse;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Holding;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;
import com.CSE310.Stock_Portefolio_Tracker.Repository.PortefolioRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.UserxRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PortfolioService {
    private final StockApiClient stockApiClient;
    private final UserxRepository userxRepository;
    private final PortefolioRepository portefolioRepository;

 

    //Get value of stock
     public BigDecimal getCurrentValue(String symbol, int quantity) {
        GlobalQuoteResponse response = stockApiClient.getStockPrice(symbol);
        BigDecimal price = new BigDecimal(response.getQuote().getPrice().precision());
        return price.multiply(BigDecimal.valueOf(quantity));
    }


   //get Info of Stock 
     public GlobalQuoteResponse getStockInfo(String symbol) {
       GlobalQuoteResponse response = stockApiClient.getStockPrice(symbol);

        // Sécurité : Alpha Vantage peut renvoyer une réponse vide
        if (response == null ||
            response.getQuote() == null ||
            response.getQuote().getPrice() == null) {

            throw new RuntimeException(
                    "Stock introuvable ou données indisponibles pour le symbole : " + symbol
            );
        }

        return response;
     }


     //Create a portefolio
     public void createPortfolio(String email, String portfolioName) {

         Userx userx = userxRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("User not found"));

         Portefolio portefolio = new Portefolio();
         portefolio.setName_portefolio(portfolioName);
         portefolio.setUser(userx);

          this.portefolioRepository.save(portefolio);
         }






    public List<PortfolioResponse> getPortfoliosForUser(Userx user) {
        return portefolioRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

     private PortfolioResponse mapToDto(Portefolio portfolio) {
        List<HoldingResponse> holdings = portfolio.getHoldings().stream()
                .map(this::mapHoldingToDto)
                .toList();

        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getName_portefolio(),
                holdings
        );
    }

    private HoldingResponse mapHoldingToDto(Holding holding) {
        return new HoldingResponse(
                holding.getStock().getSymbol(),
                holding.getStock().getName(),
                holding.getQuantity(),
                holding.getStock().getCurrentPrice()
        );
    }


}
