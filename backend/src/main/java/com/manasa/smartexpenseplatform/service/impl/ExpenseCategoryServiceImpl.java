
package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.mapper.ExpenseCategoryMapper;
import com.manasa.smartexpenseplatform.repository.ExpenseCategoryRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.BudgetRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.ExpenseCategoryService;

@Service
@Transactional
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public ExpenseCategoryServiceImpl(
            ExpenseCategoryRepository expenseCategoryRepository,
            UserRepository userRepository,
            ExpenseRepository expenseRepository,
            BudgetRepository budgetRepository) {

        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new AccessDeniedException("Authentication required");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private ExpenseCategory getOwnedCategory(Long id, User user) {
        return expenseCategoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense category not found"));
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category name is required");
        }

        String trimmedName = name.trim();

        if (trimmedName.length() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category name cannot exceed 100 characters");
        }

        return trimmedName;
    }

    @Override
    public ExpenseCategoryResponseDTO createCategory(
            ExpenseCategoryRequestDTO request) {

        User user = getAuthenticatedUser();
        String name = validateName(request.getName());

        if (expenseCategoryRepository
                .existsByUserAndNameIgnoreCase(user, name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A category with this name already exists");
        }

        ExpenseCategory category =
                ExpenseCategoryMapper.toEntity(request, user);

        category.setName(name);
        category.setIsDefault(false);

        ExpenseCategory saved =
                expenseCategoryRepository.save(category);

        return ExpenseCategoryMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseCategoryResponseDTO> getAllCategories() {
        User user = getAuthenticatedUser();

        return expenseCategoryRepository.findByUser(user)
                .stream()
                .map(ExpenseCategoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseCategoryResponseDTO getCategoryById(Long id) {
        User user = getAuthenticatedUser();
        ExpenseCategory category = getOwnedCategory(id, user);

        return ExpenseCategoryMapper.toResponseDTO(category);
    }

    @Override
    public ExpenseCategoryResponseDTO updateCategory(
            Long id,
            ExpenseCategoryRequestDTO request) {

        User user = getAuthenticatedUser();
        ExpenseCategory category = getOwnedCategory(id, user);
        String name = validateName(request.getName());

        if (Boolean.TRUE.equals(category.getIsDefault())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Default categories cannot be edited");
        }

        if (expenseCategoryRepository
                .existsByUserAndNameIgnoreCaseAndIdNot(
                        user, name, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A category with this name already exists");
        }

        category.setName(name);

        ExpenseCategory updated =
                expenseCategoryRepository.save(category);

        return ExpenseCategoryMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        User user = getAuthenticatedUser();
        ExpenseCategory category = getOwnedCategory(id, user);

        if (Boolean.TRUE.equals(category.getIsDefault())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Default categories cannot be deleted");
        }

        if (expenseRepository.existsByExpenseCategory(category)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This category is used by existing expenses. "
                    + "Reassign those expenses before deleting it.");
        }

        if (budgetRepository.existsByExpenseCategory(category)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This category is used by existing budgets. "
                    + "Remove or reassign those budgets before deleting it.");
        }

        expenseCategoryRepository.delete(category);
    }
}
