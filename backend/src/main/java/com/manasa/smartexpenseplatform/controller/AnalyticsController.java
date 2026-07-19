package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manasa.smartexpenseplatform.dto.BudgetAnalyticsDTO;
import com.manasa.smartexpenseplatform.service.AnalyticsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/analytics")
@Tag(
    name = "Analytics",
    description = "APIs for expense analytics, budget insights, and financial statistics"
)
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/budget")
    public List<BudgetAnalyticsDTO> getBudgetAnalytics() {
        return analyticsService.getBudgetAnalytics();
    }
}