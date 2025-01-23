package com.jefri.EWalletSystem;

import com.jefri.EWalletSystem.entity.Transaction;
import com.jefri.EWalletSystem.entity.User;
import com.jefri.EWalletSystem.model.TransactionRequest;
import com.jefri.EWalletSystem.repository.TransactionRepository;
import com.jefri.EWalletSystem.repository.UserRepository;
import com.jefri.EWalletSystem.service.TransactionService;
import com.jefri.EWalletSystem.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class TransactionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Buat user dengan saldo awal 0
        user = new User();
        user.setUsername("testUser");
        user.setBalance(BigDecimal.ZERO);

        // Mock repository behavior
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
    }

    @Test
    public void testConcurrentTransactions() throws InterruptedException, ExecutionException {
        int numThreads = 100;
        int numTransactionsPerThread = 100;
        BigDecimal transactionAmount = BigDecimal.valueOf(1000);

            // Simulasikan 100 thread yang melakukan transaksi
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            CountDownLatch latch = new CountDownLatch(numThreads);

            // Membuat task untuk setiap thread yang melakukan transaksi kredit
            Callable<Void> task = () -> {
                for (int i = 0; i < numTransactionsPerThread; i++) {
                    // Buat transaction request dengan tipe CREDIT
                    TransactionRequest transactionRequest = new TransactionRequest();
                    transactionRequest.setUsername("testUser");
                    transactionRequest.setAmount(transactionAmount);
                    transactionRequest.setType(Transaction.TransactionType.CREDIT);

                    // Panggil metode untuk melakukan transaksi
                    transactionService.credit(transactionRequest);
                }
                latch.countDown(); // Mengurangi countDown setiap thread selesai
                return null;
            };

            // Jalankan semua thread secara bersamaan
            List<Callable<Void>> tasks = new ArrayList<>(numThreads);
            for (int i = 0; i < numThreads; i++) {
                tasks.add(task);
            }

            // Eksekusi semua tasks (transaksi)
            executor.invokeAll(tasks);
            latch.await(); // Tunggu hingga semua thread selesai

            // Verifikasi saldo akhir
            assertEquals(BigDecimal.valueOf(10_000_000), user.getBalance(), "Saldo akhir harus 10.000.000");
            executor.shutdown();

            System.out.println("EXPECTED : " + BigDecimal.valueOf(10_000_000));

            System.out.println("ACTUAL : " + user.getBalance());


    }

}
