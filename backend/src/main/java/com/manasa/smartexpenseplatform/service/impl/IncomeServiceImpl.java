package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.entity.Income;
import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.dto.IncomeRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeResponseDTO;
import com.manasa.smartexpenseplatform.mapper.IncomeMapper;
import com.manasa.smartexpenseplatform.repository.IncomeCategoryRepository;
import com.manasa.smartexpenseplatform.repository.IncomeRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.IncomeService;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;

    public IncomeServiceImpl(
            IncomeRepository incomeRepository,
            IncomeCategoryRepository incomeCategoryRepository,
            UserRepository userRepository) {

        this.incomeRepository = incomeRepository;
        this.incomeCategoryRepository = incomeCategoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public IncomeResponseDTO createIncome(IncomeRequestDTO request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    IncomeCategory category = incomeCategoryRepository
            .findById(request.getIncomeCategoryId())
            .orElseThrow(() -> new RuntimeException("Income category not found"));

    Income income = IncomeMapper.toEntity(
            request,
            user,
            category
    );

    Income savedIncome = incomeRepository.save(income);

        return IncomeMapper.toResponseDTO(savedIncome);
    }

    @Override
    public List<IncomeResponseDTO> getAllIncome() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return incomeRepository.findByUser(user)
            .stream()
            .map(IncomeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public IncomeResponseDTO getIncomeById(Long id) {

    Income income = incomeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Income not found"));

    return IncomeMapper.toResponseDTO(income);
    }

    @Override
    public IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO request) {

    Income income = incomeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Income not found"));

    IncomeCategory category = incomeCategoryRepository
            .findById(request.getIncomeCategoryId())
            .orElseThrow(() -> new RuntimeException("Income category not found"));

    income.setIncomeCategory(category);
    income.setAmount(request.getAmount());
    income.setSourceName(request.getSourceName());
    income.setDescription(request.getDescription());
    income.setIncomeDate(request.getIncomeDate());

    Income updatedIncome = incomeRepository.save(income);

        return IncomeMapper.toResponseDTO(updatedIncome);
    }

    @Override
    public void deleteIncome(Long id) {

    Income income = incomeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Income not found"));

    incomeRepository.delete(income);
    }

}
