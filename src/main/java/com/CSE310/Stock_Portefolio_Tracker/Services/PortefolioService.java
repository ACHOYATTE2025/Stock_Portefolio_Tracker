package com.CSE310.Stock_Portefolio_Tracker.Services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.HoldingResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.PortfolioResponse;
import com.CSE310.Stock_Portefolio_Tracker.Dto.ResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Holding;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Portefolio;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;
import com.CSE310.Stock_Portefolio_Tracker.Repository.PortefolioRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.UserxRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

@Service
@AllArgsConstructor
@Slf4j
public class PortefolioService {
    private final StockApiClient stockApiClient;
    private final UserxRepository userxRepository;
    private final PortefolioRepository portefolioRepository;
    

 

    
     //Create a portefolio
     public void createPortfolio(String email, String portfolioName,double amount) {

         Userx userx = userxRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("User not found"));

        Portefolio portefolio = new Portefolio();
        portefolio.setNamePortefolio(portfolioName);
        portefolio.setUser(userx);

        this.portefolioRepository.save(portefolio);
         }
         

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


    
    //Maping from Portfolio to PortefolioResponse
    private PortfolioResponse mapToDto(Portefolio portfolio) {
        List<HoldingResponse> holdings = portfolio.getHoldings().stream()
                .map(this::mapHoldingToDto)
                .toList();

        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getNamePortefolio(),
                holdings
        );
    }


    
    //Maping to holding to HoldingResponse
    private HoldingResponse mapHoldingToDto(Holding holding) {
        return new HoldingResponse(
                holding.getStock().getSymbol(),
                holding.getStock().getName(),
                holding.getQuantity(),
                holding.getStock().getCurrentPrice()               
        );
    }



    // Lire un extrait ou les extraits
    public List<PortfolioResponse> getPortfolio(String num) {
    
        boolean notEmpty = Strings.isNotEmpty(num);
        Userx usex = (Userx) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(notEmpty)
            { Optional<Portefolio> porto= this.portefolioRepository.findByNamePortefolio(num);
                if(porto==null){throw new RuntimeException("PORTFOLIO DOESN'T EXIST");}
            
            return this.portefolioRepository.findByNamePortefolioAndUser(num,usex).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            }



                List<Portefolio> portolio = (List<Portefolio>) this.portefolioRepository.findAllByUser(usex);
                if(portolio.isEmpty()){throw new RuntimeException( "NOTHING TO SHOW") ; }
        return portolio.stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
            
    }




    //get portfolio by Id
        public Optional<PortfolioResponse> getBirthById(Long id) {
            
            if(id==null){throw new RuntimeException("ID none enter");}
            Optional<Portefolio> portofo = this.portefolioRepository.findById(id);
            if(portofo.isEmpty()){throw new RuntimeException("PORTFOLIO DOESN'T EXIST!!!");}

            Optional<PortfolioResponse> alex=  portofo.stream().map(this::mapToDto).findFirst();
            return alex;
            }




    //portfolio deletion   
        @Transactional
        public ResponseEntity<ResponseDto> portfoliodeletion(String num) {
            Userx usex = (Userx) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
            Optional<Portefolio> DEXO = this.portefolioRepository.findByNamePortefolio(num);

            if(DEXO== null){
                throw new RuntimeException( "DELETION IMPOSSIBLE") ;
            }else{
                this.portefolioRepository.delete(DEXO.get());
            }
            
             
 
            log.info("XTRAIT :"+DEXO);
     

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(406, "PORTEFOLIO ° "+DEXO.get().getNamePortefolio() +" A ETE SUPPRIME" ,"DONE"));
           
        }

}
