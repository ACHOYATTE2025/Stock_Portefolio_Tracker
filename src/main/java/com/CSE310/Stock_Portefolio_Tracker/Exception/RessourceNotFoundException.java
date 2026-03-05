package com.CSE310.Stock_Portefolio_Tracker.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;




// gestion des messages d'exeption
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RessourceNotFoundException extends RuntimeException{

    
public RessourceNotFoundException(String message){
       super(message);
    }

}
