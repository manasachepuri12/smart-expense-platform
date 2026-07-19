package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.ExpenseTargetRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseTargetResponseDTO;
import com.manasa.smartexpenseplatform.entity.ExpenseTarget;
import com.manasa.smartexpenseplatform.mapper.ExpenseTargetMapper;
import com.manasa.smartexpenseplatform.repository.ExpenseTargetRepository;
import com.manasa.smartexpenseplatform.service.ExpenseTargetService;

@Service
public class ExpenseTargetServiceImpl implements ExpenseTargetService {

    private final ExpenseTargetRepository expenseTargetRepository;

    public ExpenseTargetServiceImpl(
            ExpenseTargetRepository expenseTargetRepository) {

        this.expenseTargetRepository = expenseTargetRepository;
    }

    @Override
    public ExpenseTargetResponseDTO createTarget(
            ExpenseTargetRequestDTO request) {

        ExpenseTarget target =
                ExpenseTargetMapper.toEntity(request);

        return ExpenseTargetMapper.toResponseDTO(
                expenseTargetRepository.save(target));
    }

    @Override
    public List<ExpenseTargetResponseDTO> getAllTargets() {

        return expenseTargetRepository.findAll()
                .stream()
                .map(ExpenseTargetMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseTargetResponseDTO getTargetById(Long id) {

        ExpenseTarget target = expenseTargetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense Target not found"));

        return ExpenseTargetMapper.toResponseDTO(target);
    }

    @Override
    public ExpenseTargetResponseDTO updateTarget(
            Long id,
            ExpenseTargetRequestDTO request) {

        ExpenseTarget target = expenseTargetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense Target not found"));

        target.setName(request.getName());

        return ExpenseTargetMapper.toResponseDTO(
                expenseTargetRepository.save(target));
    }

    @Override
    public void deleteTarget(Long id) {

        ExpenseTarget target = expenseTargetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense Target not found"));

        expenseTargetRepository.delete(target);
    }
}