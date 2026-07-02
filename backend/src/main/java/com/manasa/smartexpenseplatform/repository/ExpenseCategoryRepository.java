package com.manasa.smartexpenseplatform.repository;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseCategoryRepository
extends JpaRepository<ExpenseCategory, Long> {

    List<ExpenseCategory> findByUser(User user);
}
