package com.CSE310.Stock_Portefolio_Tracker.Unit.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionRequest;
import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionResponse;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Stock;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Transactions;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Wallet;
import com.CSE310.Stock_Portefolio_Tracker.Enum.TransactionType;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {



    @Test
    void executeTransaction_buyStock_success() {

        // Arrange
        Userx user = new Userx();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setAmount(new BigDecimal("10000"));
        wallet.setUserx(user);

        Portefolio portfolio = new Portefolio();
        portfolio.setId(1L);
        portfolio.setUser(user);

        Stock stock = new Stock();
        stock.setSymbol("AAPL");

        TransactionRequest request = new TransactionRequest();
        request.setSymbol("AAPL");
        request.setQuantity(10);
        request.setType(TransactionType.BUY);

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        // Mock repositories
        when(walletRepository.findByUserx(user)).thenReturn(wallet);
        when(portefolioRepository.findByUser(user)).thenReturn(Optional.of(portfolio));
        when(stockRepository.findBySymbol("AAPL")).thenReturn(Optional.of(stock));

        // Mock API response
        GlobalQuoteResponse response = new GlobalQuoteResponse();
        GlobalQuote quote = new GlobalQuote();
        quote.setPrice("100");
        response.setQuote(quote);

        when(stockApiClient.getStockPrice("AAPL")).thenReturn(response);

        // Mock holding
        when(holdingRepository.findByPortfolioAndStock(portfolio, stock))
                .thenReturn(Optional.empty());

        Transactions transaction = new Transactions();
        transaction.setStock(stock);
        transaction.setQuantity(10);
        transaction.setPrice(new BigDecimal("100"));
        transaction.setType(TransactionType.BUY);

        when(transactionsRepository.save(any())).thenReturn(transaction);

        // Act
        TransactionResponse result =
                transactionService.executeTransaction(request);

        // Assert
        assertEquals("AAPL", result.getSymbol());
        assertEquals(10, result.getQuantity());

        verify(walletRepository).save(any());
        verify(holdingRepository).save(any());
        verify(transactionsRepository).save(any());
    }

}
