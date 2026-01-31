package com.CSE310.Stock_Portefolio_Tracker.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.PortfolioResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.ResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.Services.PortfolioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor

@Tag(
  name = "Stock Controller",
  description="STOCK CONTROLLER  Api in Stock tracker management APP to get  details"
)
public class StockController {

    private final PortfolioService portfolioService;



    
          
//Get Stock Info
  @Operation(
    summary="REST API to get Stock's Informations",
    description = "REST API to get Stock's information in  Stock tracker management APP "
  )

  @ApiResponse(
    responseCode="200",
    description = "INFORMATION DONE"
  )
    @GetMapping("/{symbol}")
    public GlobalQuoteResponse getStock(@PathVariable String symbol) {
        return portfolioService.getStockInfo(symbol);
    }



          
//Get value of stock
  @Operation(
    summary="REST API to get value of Stock",
    description = "REST API to cget value  inside Stock  "
  )

  @ApiResponse(
    responseCode="200",
    description = "VALUE DONE"
  )
        @GetMapping("/{symbol}/{quantity}")
        public BigDecimal getStockValue(
                @PathVariable String symbol,
                @PathVariable int quantity) {

            return portfolioService.getCurrentValue(symbol, quantity);
        }


        @PostMapping("/createPortefolio")
        public ResponseEntity<ResponseDto> createPortfolio(@RequestParam String email, @RequestParam String porteFolioName) {

             portfolioService.createPortfolio(email,porteFolioName);

             return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseDto(201,"PORTEFOLIO CREATED","STOCK DONE"));
       

    }

    //Get All portefolio of User
     @GetMapping("/api/portfolio")
    public List<PortfolioResponse> getMyPortfolios(@AuthenticationPrincipal Userx user) {
        return portfolioService.getPortfoliosForUser(user);
    }

}
