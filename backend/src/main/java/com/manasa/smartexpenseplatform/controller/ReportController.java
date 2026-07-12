package com.manasa.smartexpenseplatform.controller;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.MonthlyReportDTO;
import com.manasa.smartexpenseplatform.service.ReportService;

@RestController
@RequestMapping("/api/reports")
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