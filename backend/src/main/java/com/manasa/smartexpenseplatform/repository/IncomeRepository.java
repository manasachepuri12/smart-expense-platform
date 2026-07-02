package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.Income;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);
    List<Income> findByIncomeDateBetween(LocalDate startDate, LocalDate endDate);
    List<Income> findByUserAndIncomeDateBetween(
        User user,
        LocalDate startDate,
        LocalDate endDate
);
}

