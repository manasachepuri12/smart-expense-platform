package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.BudgetRequestDTO;
import com.manasa.smartexpenseplatform.dto.BudgetResponseDTO;
import com.manasa.smartexpenseplatform.entity.Budget;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.mapper.BudgetMapper;
import com.manasa.smartexpenseplatform.repository.BudgetRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseCategoryRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.BudgetService;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public BudgetServiceImpl(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            ExpenseCategoryRepository expenseCategoryRepository) {

        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    @Override
    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ExpenseCategory category = expenseCategoryRepository
                .findById(request.getExpenseCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense category not found"));

        Budget budget = BudgetMapper.toEntity(
                request,
                user,
                category
        );

        Budget savedBudget = budgetRepository.save(budget);

        return BudgetMapper.toResponseDTO(savedBudget);
    }

    @Override
    public List<BudgetResponseDTO> getAllBudgets() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return budgetRepository.findByUser(user)
                .stream()
                .map(BudgetMapper::toResponseDTO)
                .toList();
    }

    @Override
    public BudgetResponseDTO getBudgetById(Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        return BudgetMapper.toResponseDTO(budget);
    }

    @Override
    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO request) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        ExpenseCategory category = expenseCategoryRepository
                .findById(request.getExpenseCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense category not found"));

        budget.setBudgetAmount(request.getBudgetAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setExpenseCategory(category);

        Budget updatedBudget = budgetRepository.save(budget);

        return BudgetMapper.toResponseDTO(updatedBudget);
    }

    @Override
    public void deleteBudget(Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        budgetRepository.delete(budget);
    }
}