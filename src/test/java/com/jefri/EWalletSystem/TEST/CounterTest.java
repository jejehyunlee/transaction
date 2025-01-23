package com.jefri.EWalletSystem.TEST;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


class CounterTest {


@Test
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

//         Buat beberapa thread untuk menambah transaksi secara paralel

    try {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.tambahNonAtomic();
            }
        });

//        Thread t1Min = new Thread(() -> {
//            for (int i = 1000; i > 0; i--) {
//                counter.kurangNonAtomic();
//            }
//        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.tambahNonAtomic();
            }
        });

//        Thread t2Min = new Thread(() -> {
//            for (int i = 1000; i > 0; i--) {
//                counter.kurangNonAtomic();
//            }
//        });

        t1.start();
        t2.start();

        t1.join(); // Tunggu thread t1 selesai
        t2.join(); // Tunggu thread t2 selesai


        Assertions.assertEquals(2000, counter.getValuNon(), "hasil nya");

        // Cetak total transaksi
        System.out.println("Total Transactions: " + counter.getValuNon());

    }catch (Exception e){
        System.out.println(e.getMessage());
    }
}


}