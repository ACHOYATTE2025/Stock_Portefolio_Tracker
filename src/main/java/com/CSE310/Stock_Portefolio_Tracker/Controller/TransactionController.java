package com.CSE310.Stock_Portefolio_Tracker.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionRequest;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Transactions;
import com.CSE310.Stock_Portefolio_Tracker.Services.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody TransactionRequest request,Authentication authentication) {
        Transactions transaction = transactionService.executeTransaction(request, authentication);
        return ResponseEntity.ok(transaction);
    }




}
