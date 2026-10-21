
package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseCategoryRepository
        extends JpaRepository<ExpenseCategory, Long> {

    List<ExpenseCategory> findByUser(User user);

    Optional<ExpenseCategory> findByIdAndUser(Long id, User user);

    boolean existsByUserAndNameIgnoreCase(User user, String name);

    boolean existsByUserAndNameIgnoreCaseAndIdNot(
            User user, String name, Long id);
}
