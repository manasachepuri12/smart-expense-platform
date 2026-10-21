
package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeCategoryRepository
        extends JpaRepository<IncomeCategory, Long> {

    List<IncomeCategory> findByUser(User user);

    Optional<IncomeCategory> findByIdAndUser(Long id, User user);

    boolean existsByUserAndNameIgnoreCase(User user, String name);

    boolean existsByUserAndNameIgnoreCaseAndIdNot(
            User user, String name, Long id);
}
