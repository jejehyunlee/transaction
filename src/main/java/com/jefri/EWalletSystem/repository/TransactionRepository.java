package com.jefri.EWalletSystem.repository;

import com.jefri.EWalletSystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction , Long> {


}

