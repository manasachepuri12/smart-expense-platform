package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.Budget;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
    List<Budget> findByUserAndMonthAndYear(
        User user,
        Integer month,
        Integer year
    );
    @Query("""
    SELECT COALESCE(SUM(b.budgetAmount),0)
    FROM Budget b
    WHERE b.user = :user
    """)
    BigDecimal getTotalBudgetByUser(User user);
}