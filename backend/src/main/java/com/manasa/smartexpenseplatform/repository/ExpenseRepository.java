package com.manasa.smartexpenseplatform.repository;
import com.manasa.smartexpenseplatform.entity.Expense;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByExpenseCategory(ExpenseCategory expenseCategory);
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expense> findByUserAndExpenseDateBetween(
        User user,
        LocalDate startDate,
        LocalDate endDate
    );
}
