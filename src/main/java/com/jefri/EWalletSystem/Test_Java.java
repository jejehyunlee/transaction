package com.jefri.EWalletSystem;

import com.jefri.EWalletSystem.entity.Transaction;
import org.yaml.snakeyaml.scanner.Scanner;

public class Test_Java {

    public static void main(String[] args) {

        Transaction transaction = new Transaction();

        System.out.println(transaction.getAmount().toString());

    }
}
