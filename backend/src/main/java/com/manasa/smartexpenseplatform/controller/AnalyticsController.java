package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manasa.smartexpenseplatform.dto.BudgetAnalyticsDTO;
import com.manasa.smartexpenseplatform.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
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