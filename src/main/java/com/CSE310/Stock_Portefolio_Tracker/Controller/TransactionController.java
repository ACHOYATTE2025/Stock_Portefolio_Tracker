package com.CSE310.Stock_Portefolio_Tracker.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionRequest;
import com.CSE310.Stock_Portefolio_Tracker.Dto.TransactionResponse;

import com.CSE310.Stock_Portefolio_Tracker.Services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse transaction = transactionService.executeTransaction(request);
        return ResponseEntity.ok(transaction);
    }




}
