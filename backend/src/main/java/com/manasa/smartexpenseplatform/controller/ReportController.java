package com.manasa.smartexpenseplatform.controller;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.MonthlyReportDTO;
import com.manasa.smartexpenseplatform.service.ReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/reports")
@Tag(
    name = "Reports",
    description = "Monthly reports and analytics"
)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public MonthlyReportDTO getMonthlyReport(
            @RequestParam Integer month,
            @RequestParam Integer year) {

        return reportService.getMonthlyReport(month, year);
    }
}