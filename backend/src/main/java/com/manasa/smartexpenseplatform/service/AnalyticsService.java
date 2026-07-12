package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.BudgetAnalyticsDTO;

public interface AnalyticsService {

    List<BudgetAnalyticsDTO> getBudgetAnalytics();

}