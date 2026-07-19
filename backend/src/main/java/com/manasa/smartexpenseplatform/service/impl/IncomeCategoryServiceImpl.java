package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;
import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.mapper.IncomeCategoryMapper;
import com.manasa.smartexpenseplatform.repository.IncomeCategoryRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.IncomeCategoryService;

@Service
public class IncomeCategoryServiceImpl implements IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;

    public IncomeCategoryServiceImpl(
            IncomeCategoryRepository incomeCategoryRepository,
            UserRepository userRepository) {

        this.incomeCategoryRepository = incomeCategoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public IncomeCategoryResponseDTO createCategory(
            IncomeCategoryRequestDTO request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        IncomeCategory category =
                IncomeCategoryMapper.toEntity(request, user);

        IncomeCategory savedCategory =
                incomeCategoryRepository.save(category);

        return IncomeCategoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public List<IncomeCategoryResponseDTO> getAllCategories() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<IncomeCategory> categories =
                incomeCategoryRepository.findByUser(user);

        return categories.stream()
                .map(IncomeCategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncomeCategoryResponseDTO getCategoryById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        IncomeCategory category = incomeCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return IncomeCategoryMapper.toResponseDTO(category);
    }

    @Override
    public IncomeCategoryResponseDTO updateCategory(
            Long id,
            IncomeCategoryRequestDTO request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        IncomeCategory category = incomeCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        category.setName(request.getName());

        IncomeCategory updatedCategory =
                incomeCategoryRepository.save(category);

        return IncomeCategoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        IncomeCategory category = incomeCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        incomeCategoryRepository.delete(category);
    }
}