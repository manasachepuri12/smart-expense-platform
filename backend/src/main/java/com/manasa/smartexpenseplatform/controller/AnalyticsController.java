package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manasa.smartexpenseplatform.dto.BudgetAnalyticsDTO;
import com.manasa.smartexpenseplatform.service.AnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(
            summary = "Get budget analytics",
            description = "Retrieves budget analytics for the authenticated user, including budget utilization, spending trends, and remaining budget."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget analytics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/budget")
    public List<BudgetAnalyticsDTO> getBudgetAnalytics() {
        return analyticsService.getBudgetAnalytics();
    }

@GetMapping("/budget/monthly")
@Operation(
        summary = "Get monthly budget analytics",
        description = "Retrieves category-wise budget, spending, remaining amount, percentage used, and status for a selected month and year."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monthly budget analytics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
})
public List<BudgetAnalyticsDTO> getMonthlyBudgetAnalytics(
        @org.springframework.web.bind.annotation.RequestParam Integer month,
        @org.springframework.web.bind.annotation.RequestParam Integer year) {

    return analyticsService.getBudgetAnalytics(month, year);
}

}