package com.manasa.smartexpenseplatform.repository;
import com.manasa.smartexpenseplatform.entity.Expense;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
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
    @Query("""
    SELECT COALESCE(SUM(e.amount),0)
    FROM Expense e
    WHERE e.user = :user
    """)
    BigDecimal getTotalExpenseByUser(User user);
}
