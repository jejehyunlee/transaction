package com.jefri.EWalletSystem;

import com.jefri.EWalletSystem.TEST.Counter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EWalletSystemApplicationTests {

//
//	@Test
//	void testValue() throws InterruptedException {
//		var counter = new Counter();
//		Runnable runnable = () -> {
//			for (int i = 0; i < 1_000_000; i++) {
//				counter.tambah();
//			}
//        };
//
//		Thread thread1 = new Thread(runnable);
//		Thread thread2 = new Thread(runnable);
//		Thread thread3 = new Thread(runnable);
//
//		thread1.start();
//		thread2.start();
//		thread3.start();
//
//		thread1.join();
//		thread2.join();
//		thread3.join();
//
//		System.out.println( "HASIL COUNTER = " + counter.getValue());
//    }


//	private int balance = 0;
//
//	// Method to simulate credit operation
//	public void credit(int amount) {
//		balance += amount;
//	}
//
//	@Test
//	void testConcurrentCreditTransactions() throws InterruptedException {
//		int threadCount = 100;
//		int transactionsPerThread = 100;
//		int creditAmount = 1000;
//
//		Thread[] threads = new Thread[threadCount];
//
//		for (int i = 0; i < threadCount; i++) {
//			threads[i] = new Thread(() -> {
//				for (int j = 0; j < transactionsPerThread; j++) {
//					credit(creditAmount);
//				}
//			});
//		}
//
//		for (Thread thread : threads) {
//			thread.start();
//		}
//
//		for (Thread thread : threads) {
//			thread.join();
//		}
//
//		System.out.println(balance);
//
//	}




}
