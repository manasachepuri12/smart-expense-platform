
package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;
import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.mapper.IncomeCategoryMapper;
import com.manasa.smartexpenseplatform.repository.IncomeCategoryRepository;
import com.manasa.smartexpenseplatform.repository.IncomeRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.IncomeCategoryService;

@Service
@Transactional
public class IncomeCategoryServiceImpl implements IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeCategoryServiceImpl(
            IncomeCategoryRepository incomeCategoryRepository,
            IncomeRepository incomeRepository,
            UserRepository userRepository) {

        this.incomeCategoryRepository = incomeCategoryRepository;
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new AccessDeniedException("Authentication required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private IncomeCategory getOwnedCategory(Long id, User user) {
        return incomeCategoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Income category not found"));
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
    public IncomeCategoryResponseDTO createCategory(
            IncomeCategoryRequestDTO request) {

        User user = getAuthenticatedUser();
        String name = validateName(request.getName());

        if (incomeCategoryRepository
                .existsByUserAndNameIgnoreCase(user, name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An income category with this name already exists");
        }

        IncomeCategory category =
                IncomeCategoryMapper.toEntity(request, user);

        category.setName(name);
        category.setIsDefault(false);

        return IncomeCategoryMapper.toResponseDTO(
                incomeCategoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeCategoryResponseDTO> getAllCategories() {
        User user = getAuthenticatedUser();

        return incomeCategoryRepository.findByUser(user)
                .stream()
                .map(IncomeCategoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeCategoryResponseDTO getCategoryById(Long id) {
        User user = getAuthenticatedUser();

        return IncomeCategoryMapper.toResponseDTO(
                getOwnedCategory(id, user));
    }

    @Override
    public IncomeCategoryResponseDTO updateCategory(
            Long id,
            IncomeCategoryRequestDTO request) {

        User user = getAuthenticatedUser();
        IncomeCategory category = getOwnedCategory(id, user);
        String name = validateName(request.getName());

        if (Boolean.TRUE.equals(category.getIsDefault())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Default income categories cannot be edited");
        }

        if (incomeCategoryRepository
                .existsByUserAndNameIgnoreCaseAndIdNot(
                        user, name, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An income category with this name already exists");
        }

        category.setName(name);

        return IncomeCategoryMapper.toResponseDTO(
                incomeCategoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        User user = getAuthenticatedUser();
        IncomeCategory category = getOwnedCategory(id, user);

        if (Boolean.TRUE.equals(category.getIsDefault())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Default income categories cannot be deleted");
        }

        if (incomeRepository.existsByIncomeCategory(category)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This category is used by existing income records. "
                    + "Reassign those records before deleting it.");
        }

        incomeCategoryRepository.delete(category);
    }
}
