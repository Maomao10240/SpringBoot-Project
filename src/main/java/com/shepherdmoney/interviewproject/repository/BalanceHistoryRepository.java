package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
    @Query("SELECT b FROM BalanceHistory b WHERE b.date <= :date AND b.creditCard = :card ORDER BY b.date DESC LIMIT 1")
    Optional<BalanceHistory> findBalanceLessOrEqual(LocalDate date, CreditCard card);

    @Modifying
    @Transactional
    @Query("update BalanceHistory b set b.balance = b.balance + :diff WHERE b.creditCard = :creditCard AND b.date > :date")
    void updateBalanceHistory(CreditCard creditCard, double diff, LocalDate date);

    @Modifying
    @Transactional
    @Query("update BalanceHistory b set b.balance = b.balance + :diff WHERE b.creditCard = :creditCard AND b.date = :date")
    void updateBalanceOfDate(CreditCard creditCard, double diff, LocalDate date);

    //used to retrieval the balance of a single day
    @Query("SELECT b FROM BalanceHistory b WHERE b.creditCard = :creditCard AND b.date = :date")
    Optional<BalanceHistory> selectBalanceOfDate(CreditCard creditCard, LocalDate date);

}
