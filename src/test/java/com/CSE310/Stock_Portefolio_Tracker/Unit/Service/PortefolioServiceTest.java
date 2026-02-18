package com.CSE310.Stock_Portefolio_Tracker.Unit.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.CSE310.Stock_Portefolio_Tracker.Dto.SignupRequestDto;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;
import com.CSE310.Stock_Portefolio_Tracker.Repository.PortefolioRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.UserxRepository;
import com.CSE310.Stock_Portefolio_Tracker.Services.PortefolioService;



@ExtendWith(MockitoExtension.class)
class PortefolioServiceTest {

    @Mock
    private StockApiClient stockApiClient;

    @Mock
    private UserxRepository userxRepository;

    @Mock
    private PortefolioRepository portefolioRepository;

    @InjectMocks
    private PortefolioService portefolioService;

    @Test
    void createPortfolioTest() {

        // Arrange
        String email = "test@gmail.com";
        String portfolioName = "Mon Portefolio";
        double amount = 1000;

        Userx user = new Userx();
        user.setId(1L);
        user.setEmail(email);

        when(userxRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Act
        portefolioService.createPortfolio(email, portfolioName,amount);

        // Assert
        verify(portefolioRepository, times(1))
                .save(argThat(portefolio ->
                        portefolio.getName_portefolio().equals(portfolioName)
                        && portefolio.getUser().equals(user)
                ));
    }




    @Test
    void createPortfolio_userNotFound_shouldThrowException() {

        // Arrange
        String email = "inexistant@gmail.com";

        when(userxRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () ->
                portefolioService.createPortfolio(email, "Test",1000)
        );

        verify(portefolioRepository, never()).save(any());
}

}



