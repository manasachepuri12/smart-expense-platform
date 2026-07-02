package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.Budget;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
    List<Budget> findByUserAndMonthAndYear(
        User user,
        Integer month,
        Integer year
    );
}