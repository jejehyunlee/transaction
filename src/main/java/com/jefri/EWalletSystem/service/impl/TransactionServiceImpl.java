package com.jefri.EWalletSystem.service.impl;

import com.jefri.EWalletSystem.entity.Transaction;
import com.jefri.EWalletSystem.entity.User;
import com.jefri.EWalletSystem.model.TransactionRequest;
import com.jefri.EWalletSystem.model.TransactionResponse;
import com.jefri.EWalletSystem.repository.TransactionRepository;
import com.jefri.EWalletSystem.repository.UserRepository;
import com.jefri.EWalletSystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public synchronized TransactionResponse debit(TransactionRequest transactionRequest) {

        try {

                BigDecimal maxAllowed = new BigDecimal("9999999999999999999999999"); // Sesuai tipe DECIMAL di database

                Optional<User> userOptional = userRepository.findByUsername(transactionRequest.getUsername());

                if (userOptional.isEmpty()){
                    logger.info("User NOT found: {}", (Object) null);
                    return TransactionResponse.builder()
                            .message("User Not Found")
                            .build();
                }


                validateTrx(transactionRequest);

                User user = getUser(transactionRequest);


                BigDecimal currentBalance = (user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
                logger.debug("Current balance for user '{}': {}", user.getUsername(), currentBalance);

                BigDecimal transactionAmount = transactionRequest.getAmount();

                    if (transactionAmount.compareTo(BigDecimal.ZERO) == 0) {
                         logger.info("Invalid Amount :" + transactionAmount);
                        return TransactionResponse.builder()
                                .message("Invalid Ammount")
                                .build();
                    }

                     if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
                         logger.warn("Transaction amount cannot be negative: {}", transactionAmount);
                        return TransactionResponse.builder()
                                .message("Transaction amount cannot be negative")
                                .build();
                    }

                    if (maxAllowed.compareTo(maxAllowed) > 0) {
                        logger.warn("Transaction max Allowed: {}", transactionAmount);
                        return TransactionResponse.builder()
                                .message("Numeric Field Overflow")
                                .build();
                    }


                BigDecimal newBalance;

                newBalance = currentBalance.subtract(transactionAmount);
                logger.debug("Calculated new balance for user '{}': {}", user.getUsername(), newBalance);

                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    logger.warn("Insufficient balance for user '{}': Current balance {}, transaction amount {}"
                            ,user.getUsername(), currentBalance, transactionAmount);
                    return TransactionResponse.builder()
                            .message("Insufficient Balance")
                            .build();
                }

                user.setBalance(newBalance);
                userRepository.saveAndFlush(user);
                logger.info("User balance updated for user '{}'. New balance: {}", user.getUsername(), newBalance);

            Transaction transaction = new Transaction().toBuilder()
                    .user(user)
                    .amount(transactionAmount)
                    .type(Transaction.TransactionType.DEBIT)
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.saveAndFlush(transaction);
            logger.info("Transaction successfully saved. Transaction ID: {}", transaction.getId());

                // Kembalikan respons transaksi
                return getResponse(transaction, newBalance);

        }catch (RuntimeException ex) {
            logger.error("Unexpected error during transaction processing: {}", ex.getMessage(), ex);
            // Menangani kesalahan umum lainnya
            return TransactionResponse.builder()
                    .message("An error occurred: " + ex.getMessage())
                    .build();

        }
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public synchronized TransactionResponse credit(TransactionRequest transactionRequest) {

        try {

            BigDecimal maxAllowed = new BigDecimal("999999999999999.99"); // Sesuai tipe DECIMAL di database

            validateTrx(transactionRequest);

            User user = getUser(transactionRequest);

            BigDecimal currentBalance = (user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
            logger.debug("Current balance for user '{}': {}", user.getUsername(), currentBalance);


            BigDecimal transactionAmount = transactionRequest.getAmount();

            if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
                logger.info("Invalid Amount -> Cannot Be Negative :" + transactionAmount);
                return TransactionResponse.builder()
                        .message("Transaction amount cannot be negative")
                        .build();
            }

            if (maxAllowed.compareTo(maxAllowed) > 0) {
                logger.warn("Transaction max Allowed: {}", transactionAmount);
                return TransactionResponse.builder()
                        .message("Numeric Field Overflow")
                        .build();
            }

            if (transactionAmount.compareTo(BigDecimal.ZERO) == 0) {
                logger.info("Invalid Amount :" + transactionAmount);
                return TransactionResponse.builder()
                        .message("Invalid Ammount")
                        .build();
            }

            BigDecimal newBalance;

            newBalance = currentBalance.add(transactionAmount);
            logger.debug("Calculated new balance for user '{}': {}", user.getUsername(), newBalance);


            user.setBalance(newBalance);
            userRepository.saveAndFlush(user);
            logger.info("User balance updated for user '{}'. New balance: {}", user.getUsername(), newBalance);

            Transaction transaction = new Transaction().toBuilder()
                    .user(user)
                    .amount(transactionAmount)
                    .type(Transaction.TransactionType.CREDIT)
                    .createdAt(LocalDateTime.now())
                    .build();
            logger.info("Transaction successfully saved. Transaction ID: {}", transaction.getId());
            transactionRepository.saveAndFlush(transaction);

            // Kembalikan respons transaksi
            return getResponse(transaction , newBalance);


        } catch (DataAccessException | DataException ex) {
            logger.error("Unexpected error during transaction processing: {}", ex.getMessage(), ex);
            // Menangani kesalahan umum lainnya
            return TransactionResponse.builder()
                    .message("An error occurred: " + ex.getMessage())
                    .build();
        }

    }

    private static TransactionResponse getResponse(Transaction transaction, BigDecimal newBalance) {
        return TransactionResponse.builder()
                .message("sukses")
                .trxId(transaction.getId())
                .new_balance(newBalance)
                .build();
    }


    private User getUser(TransactionRequest transactionRequest) {
        return userRepository.findByUsername(transactionRequest.getUsername())
                .orElseGet(() -> createDefaultUser(transactionRequest.getUsername()));
    }

    private static void validateTrx(TransactionRequest transactionRequest) {
        if (transactionRequest == null || transactionRequest.getUsername() == null || transactionRequest.getAmount() == null) {
            throw new IllegalArgumentException("Transaction request is invalid");
        }
    }

    private User createDefaultUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.ZERO); // Set default balance
        return userRepository.save(user);
    }


}


