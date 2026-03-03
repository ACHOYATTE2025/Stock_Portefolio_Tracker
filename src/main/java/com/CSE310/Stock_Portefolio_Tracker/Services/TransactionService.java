package com.CSE310.Stock_Portefolio_Tracker.Services;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionRequest;
import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionResponse;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Holding;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Stock;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Transactions;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.Enum.TransactionType;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;
import com.CSE310.Stock_Portefolio_Tracker.Repository.HoldingRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.PortefolioRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.StockRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.TransactionRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.UserxRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionsRepository;
    private final HoldingRepository holdingRepository;
    private final StockRepository stockRepository;
    private final PortefolioRepository portefolioRepository;
    private final UserxRepository userxRepository;
    private final StockApiClient stockApiClient;

 


    @Transactional
    public TransactionResponse executeTransaction(TransactionRequest request) {

        
        
        // 1️⃣ Récupérer utilisateur connecté
        Userx userx = (Userx) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userx.getUsername();
        log.info("username:"+ username);
        Userx user = this.userxRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Récupérer portfolio du user
        Portefolio portfolio = portefolioRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // 3️⃣ Récupérer stock via SYMBOL
        Stock stock = stockRepository.findBySymbol(request.getSymbol())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

            // 4️⃣ Prix réel (exemple simple)
                GlobalQuoteResponse response = stockApiClient.getStockPrice(stock.getSymbol());

            if (response == null || response.getQuote() == null) {
                throw new RuntimeException("Stock price unavailable from API");
            }

            BigDecimal price = new BigDecimal(response.getQuote().getPrice());




        // 5️⃣ Récupérer ou créer holding
        Holding holding = holdingRepository
                .findByPortfolioAndStock(portfolio, stock)
                .orElseGet(() -> {
                    Holding newHolding = new Holding();
                    newHolding.setPortfolio(portfolio);
                    newHolding.setStock(stock);
                    newHolding.setQuantity(0);
                    return newHolding;
                });

        // 6️⃣ Logique BUY / SELL
        if (request.getType() == TransactionType.BUY) {

            holding.setQuantity(holding.getQuantity() + request.getQuantity());

        } else if (request.getType() == TransactionType.SELL) {

            if (holding.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Not enough shares to sell");
            }

            holding.setQuantity(holding.getQuantity() - request.getQuantity());

            if (holding.getQuantity() == 0) {
                holdingRepository.delete(holding);
            } else {
                holdingRepository.save(holding);
            }
        }

        // 7️⃣ Sauvegarder holding si BUY
        if (request.getType() == TransactionType.BUY) {
            holdingRepository.save(holding);
        }

        // 8️⃣ Enregistrer transaction
        Transactions transaction = new Transactions();
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(price);
        transaction.setType(request.getType());

       Transactions saved = transactionsRepository.save(transaction);

        return new TransactionResponse(
                saved.getStock().getSymbol(),
                saved.getQuantity(),
                saved.getPrice(),
                saved.getType().name()
        );

}
}
