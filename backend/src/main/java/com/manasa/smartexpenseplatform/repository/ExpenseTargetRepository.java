package com.manasa.smartexpenseplatform.repository;
import com.manasa.smartexpenseplatform.entity.ExpenseTarget;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ExpenseTargetRepository
extends JpaRepository<ExpenseTarget, Long> {

}

