package com.devsu.hackerearth.backend.account.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
        @Query(value = "select t from Transaction t join t.account a " +
                        "where a.clientId = :clientId and t.date between :startDate and :endDate")
        List<Transaction> findTransactionsByClientIdAndDateRange(
                        @Param("clientId") Long clientId,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        Optional<Transaction> findTopByAccountOrderByDateDesc(Account account);

}
