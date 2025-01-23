package com.jefri.EWalletSystem.controller;


import com.jefri.EWalletSystem.model.TransactionRequest;
import com.jefri.EWalletSystem.model.TransactionResponse;
import com.jefri.EWalletSystem.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponse> credit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.credit(request));
    }

    @PostMapping("/debit")
    public ResponseEntity<TransactionResponse> debit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.debit(request));
    }


}
