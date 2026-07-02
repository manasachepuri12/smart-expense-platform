package com.manasa.smartexpenseplatform.repository;

import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeCategoryRepository
        extends JpaRepository<IncomeCategory, Long> {

    List<IncomeCategory> findByUser(User user);
}