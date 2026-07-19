package com.manasa.smartexpenseplatform.controller;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.MonthlyReportDTO;
import com.manasa.smartexpenseplatform.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@Tag(
        name = "Reports",
        description = "APIs for generating financial reports and summaries"
)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Generate monthly financial report",
            description = "Generates a detailed monthly financial report for the authenticated user, including total income, total expenses, savings, budget utilization, highest income source, highest expense, most spent category, and budget status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monthly report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid month or year"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/monthly")
    public MonthlyReportDTO getMonthlyReport(
            @RequestParam Integer month,
            @RequestParam Integer year) {

        return reportService.getMonthlyReport(month, year);
    }
}