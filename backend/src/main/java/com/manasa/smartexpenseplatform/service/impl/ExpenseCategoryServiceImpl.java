package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.mapper.ExpenseCategoryMapper;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;
import com.manasa.smartexpenseplatform.repository.ExpenseCategoryRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.ExpenseCategoryService;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    public ExpenseCategoryServiceImpl(
            ExpenseCategoryRepository expenseCategoryRepository,
            UserRepository userRepository) {

        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
    }
    @Override
    public ExpenseCategoryResponseDTO createCategory(
        ExpenseCategoryRequestDTO request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    ExpenseCategory category =
            ExpenseCategoryMapper.toEntity(request, user);

    ExpenseCategory savedCategory =
            expenseCategoryRepository.save(category);

    return ExpenseCategoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public List<ExpenseCategoryResponseDTO> getAllCategories() {

    Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<ExpenseCategory> categories =
            expenseCategoryRepository.findByUser(user);

    return categories.stream()
            .map(ExpenseCategoryMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    @Override
    public ExpenseCategoryResponseDTO getCategoryById(Long id) {

    Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    ExpenseCategory category = expenseCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    if (!category.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Access denied");
    }

    return ExpenseCategoryMapper.toResponseDTO(category);
    }

    @Override
    public ExpenseCategoryResponseDTO updateCategory(
        Long id,
        ExpenseCategoryRequestDTO request) {

    Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    ExpenseCategory category = expenseCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    if (!category.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Access denied");
    }

    category.setName(request.getName());

    ExpenseCategory updatedCategory =
            expenseCategoryRepository.save(category);

    return ExpenseCategoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

    Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    ExpenseCategory category = expenseCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    if (!category.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Access denied");
    }

    expenseCategoryRepository.delete(category);
    }
}
