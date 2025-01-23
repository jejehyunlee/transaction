package com.jefri.EWalletSystem.TEST;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    private final ReentrantLock lock = new ReentrantLock();

    AtomicLong value = new AtomicLong(0);

    public void tambah(){
        value.incrementAndGet();

    }
    public Long getValue(){
        return value.get();
    }


    private Long valueNonAtomic = 0l;

    public void tambahNonAtomic(){

        try {
            valueNonAtomic++;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public void kurangNonAtomic(){
        lock.lock();
        try {
            valueNonAtomic--;
        } finally {
            lock.unlock();
        }

    }

    public Long getValuNon(){
        return valueNonAtomic;
    }

}
