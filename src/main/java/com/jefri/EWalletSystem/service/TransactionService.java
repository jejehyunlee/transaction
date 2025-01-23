package com.jefri.EWalletSystem.service;

import com.jefri.EWalletSystem.model.TransactionRequest;
import com.jefri.EWalletSystem.model.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public interface TransactionService {

    TransactionResponse debit (TransactionRequest transactionRequest);

    TransactionResponse credit (TransactionRequest transactionRequest);


}
