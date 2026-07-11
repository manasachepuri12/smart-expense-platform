package com.manasa.smartexpenseplatform.service.impl;
import com.manasa.smartexpenseplatform.entity.Expense;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.ExpenseTarget;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.mapper.ExpenseMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;
import com.manasa.smartexpenseplatform.repository.ExpenseCategoryRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseTargetRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseTargetRepository expenseTargetRepository;
    public ExpenseServiceImpl(
        ExpenseRepository expenseRepository,
        UserRepository userRepository,
        ExpenseCategoryRepository expenseCategoryRepository,
        ExpenseTargetRepository expenseTargetRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseTargetRepository = expenseTargetRepository;
    }
    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO request) {
        Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new RuntimeException("User not found"));
        ExpenseCategory category = expenseCategoryRepository
        .findById(request.getExpenseCategoryId())
        .orElseThrow(() ->
                new RuntimeException("Expense category not found"));
        ExpenseTarget target = expenseTargetRepository
        .findById(request.getExpenseTargetId())
        .orElseThrow(() ->
                new RuntimeException("Expense target not found"));
        Expense expense = ExpenseMapper.toEntity(
            request,
            user,
            category,
            target
        );
        Expense savedExpense = expenseRepository.save(expense);
        return ExpenseMapper.toResponseDTO(savedExpense);
    }

    @Override
    public List<ExpenseResponseDTO> getAllExpenses() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user)
                .stream()
                .map(ExpenseMapper::toResponseDTO)
                .toList();
        }

        @Override
    public ExpenseResponseDTO getExpenseById(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        return ExpenseMapper.toResponseDTO(expense);
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO request) {

        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));

        ExpenseCategory category = expenseCategoryRepository
            .findById(request.getExpenseCategoryId())
            .orElseThrow(() -> new RuntimeException("Expense category not found"));

        ExpenseTarget target = expenseTargetRepository
            .findById(request.getExpenseTargetId())
            .orElseThrow(() -> new RuntimeException("Expense target not found"));

        expense.setExpenseCategory(category);
        expense.setExpenseTarget(target);
        expense.setAmount(request.getAmount());
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setExpenseDate(request.getExpenseDate());

        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponseDTO(updatedExpense);
    }
}