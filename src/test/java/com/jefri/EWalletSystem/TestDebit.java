package com.jefri.EWalletSystem;

import com.jefri.EWalletSystem.entity.Transaction;
import com.jefri.EWalletSystem.entity.User;
import com.jefri.EWalletSystem.model.TransactionRequest;
import com.jefri.EWalletSystem.model.TransactionResponse;
import com.jefri.EWalletSystem.repository.TransactionRepository;
import com.jefri.EWalletSystem.repository.UserRepository;
import com.jefri.EWalletSystem.service.TransactionService;
import com.jefri.EWalletSystem.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TestDebit {

        @Mock
        private UserRepository userRepository;

        @Mock
        private TransactionRepository transactionRepository;

        @InjectMocks
        private TransactionServiceImpl transactionService;

        private TransactionRequest request;
        private User user;

        @BeforeEach
        void setUp() {
            // Inisialisasi request dan user
            request = new TransactionRequest();
            request.setUsername("testUser");
            request.setAmount(new BigDecimal("100.00"));
            request.setType(Transaction.TransactionType.DEBIT);

            user = new User();
            user.setUsername("testUser");
            user.setBalance(new BigDecimal("500.00"));
        }

        @Test
        void testUserNotFound() {
            // Simulasi user tidak ditemukan
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

            TransactionResponse response = transactionService.credit(request);

            assertEquals("User Not Found", response.getMessage());
            verify(userRepository, times(1)).findByUsername("testUser");
        }

        @Test
        void testInvalidAmountZero() {
            // Simulasi input transaksi amount = 0
            request.setAmount(BigDecimal.ZERO);
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            TransactionResponse response = transactionService.credit(request);

            assertEquals("Invalid Ammount", response.getMessage());
        }

        @Test
        void testNegativeTransactionAmount() {
            // Simulasi input transaksi amount < 0
            request.setAmount(new BigDecimal("-10.00"));
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            TransactionResponse response = transactionService.credit(request);

            assertEquals("Transaction amount cannot be negative", response.getMessage());
        }

        @Test
        void testInsufficientBalance() {
            // Simulasi saldo tidak mencukupi
            request.setAmount(new BigDecimal("600.00"));
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            TransactionResponse response = transactionService.credit(request);

            assertEquals("Insufficient Balance", response.getMessage());
        }

        @Test
        void testTransactionOverflow() {
            // Simulasi overflow (melebihi batas maksimum allowed)
            request.setAmount(new BigDecimal("99999999999999999999999999")); // Lebih dari maxAllowed
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            TransactionResponse response = transactionService.credit(request);

            assertEquals("Numeric Field Overflow", response.getMessage());
        }

        @Test
        void testSuccessfulDebitTransaction() {
            // Simulasi transaksi debit sukses
            request.setAmount(new BigDecimal("100.00"));
            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
            when(transactionRepository.saveAndFlush(any(Transaction.class))).thenAnswer(invocation -> {
                Transaction transaction = invocation.getArgument(0);
                transaction.setId(1L); // Simulasi ID transaksi
                return transaction;
            });

            TransactionResponse response = transactionService.credit(request);

            assertEquals("sukses", response.getMessage());
            assertEquals(new BigDecimal("400.00"), response.getNew_balance());
            verify(userRepository, times(1)).saveAndFlush(any(User.class));
            verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
        }
    }

