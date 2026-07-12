package com.manasa.smartexpenseplatform.service;

import com.manasa.smartexpenseplatform.dto.MonthlyReportDTO;

public interface ReportService {

    MonthlyReportDTO getMonthlyReport(Integer month, Integer year);

}