
checkAuth();

let expenseChartInstance = null;
let barChartInstance = null;
let lineChartInstance = null;

let allIncomes = [];
let allExpenses = [];

// ------------------------------
// Helpers
// ------------------------------

function formatCurrency(amount) {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(Number(amount) || 0);
}

async function fetchAnalyticsData(endpoint) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${getToken()}`,
            Accept: "application/json"
        }
    });

    if (!response.ok) {
        throw new Error(
            `${endpoint} failed with HTTP ${response.status}`
        );
    }

    return response.json();
}

function getRecordAmount(record) {
    return Number(record.amount) || 0;
}

function getRecordCategory(record, type) {
    const category = type === "expense"
        ? record.expenseCategory
        : record.incomeCategory;

    if (typeof category === "string") {
        return category;
    }

    return category?.name || "Uncategorized";
}

function getRecordDate(record, type) {
    return type === "expense"
        ? record.expenseDate
        : record.incomeDate;
}

function parseRecordDate(record, type) {
    const dateString = getRecordDate(record, type);

    if (!dateString) {
        return null;
    }

    // Avoid timezone shifts for date-only values such as 2026-10-05.
    const date = new Date(`${dateString}T00:00:00`);

    return Number.isNaN(date.getTime()) ? null : date;
}

function getSelectedPeriod() {
    const monthValue =
        document.getElementById("analyticsMonth").value;

    const yearValue =
        document.getElementById("analyticsYear").value;

    return {
        month: monthValue === "all"
            ? null
            : Number(monthValue),
        year: Number(yearValue)
    };
}

function filterRecordsByPeriod(records, type, period) {
    return records.filter(record => {
        const date = parseRecordDate(record, type);

        if (!date || date.getFullYear() !== period.year) {
            return false;
        }

        return period.month === null ||
            date.getMonth() === period.month;
    });
}

// ------------------------------
// Chart cleanup
// ------------------------------

function destroyChart(canvasId, instance) {
    const canvas = document.getElementById(canvasId);

    if (instance) {
        instance.destroy();
    }

    if (canvas) {
        const existingChart = Chart.getChart(canvas);

        if (existingChart) {
            existingChart.destroy();
        }
    }
}

// ------------------------------
// Initialize filter controls
// ------------------------------

function initializeFilters() {
    const monthSelect =
        document.getElementById("analyticsMonth");

    const yearSelect =
        document.getElementById("analyticsYear");

    const currentYear = new Date().getFullYear();

    // Include years present in the transaction history.
    const years = new Set([currentYear]);

    [...allIncomes, ...allExpenses].forEach(record => {
        const type = allIncomes.includes(record)
            ? "income"
            : "expense";

        const date = parseRecordDate(record, type);

        if (date) {
            years.add(date.getFullYear());
        }
    });

    yearSelect.innerHTML = "";

    [...years]
        .sort((a, b) => b - a)
        .forEach(year => {
            const option = document.createElement("option");
            option.value = String(year);
            option.textContent = String(year);
            yearSelect.appendChild(option);
        });

    yearSelect.value = String(currentYear);
    monthSelect.value = String(new Date().getMonth());

    document
        .getElementById("applyAnalyticsFilter")
        .addEventListener("click", updateAnalytics);

    document
        .getElementById("resetAnalyticsFilter")
        .addEventListener("click", () => {
            monthSelect.value = "all";
            yearSelect.value = String(currentYear);
            updateAnalytics();
        });
}

// ------------------------------
// Summary cards
// ------------------------------

function updateSummary(incomes, expenses) {
    const totalIncome = incomes.reduce(
        (sum, item) => sum + getRecordAmount(item),
        0
    );

    const totalExpense = expenses.reduce(
        (sum, item) => sum + getRecordAmount(item),
        0
    );

    const savings = totalIncome - totalExpense;

    const savingsRate = totalIncome > 0
        ? (savings / totalIncome) * 100
        : 0;

    document.getElementById("totalIncome").textContent =
        formatCurrency(totalIncome);

    document.getElementById("totalExpense").textContent =
        formatCurrency(totalExpense);

    document.getElementById("totalSavings").textContent =
        formatCurrency(savings);

    document.getElementById("savingsRate").textContent =
        `${savingsRate.toFixed(1)}%`;
}

// ------------------------------
// Expense distribution pie chart
// ------------------------------

function renderExpenseChart(expenses) {
    destroyChart("expenseChart", expenseChartInstance);
    expenseChartInstance = null;

    const categoryTotals = {};

    expenses.forEach(expense => {
        const category = getRecordCategory(expense, "expense");

        categoryTotals[category] =
            (categoryTotals[category] || 0) +
            getRecordAmount(expense);
    });

    const labels = Object.keys(categoryTotals);
    const values = Object.values(categoryTotals);

    expenseChartInstance = new Chart(
        document.getElementById("expenseChart"),
        {
            type: "pie",
            data: {
                labels,
                datasets: [{
                    label: "Expenses",
                    data: values
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        position: "bottom"
                    },
                    tooltip: {
                        callbacks: {
                            label(context) {
                                return `${context.label}: ${
                                    formatCurrency(context.raw)
                                }`;
                            }
                        }
                    }
                }
            }
        }
    );
}

// ------------------------------
// Monthly charts for selected year
// ------------------------------

function renderMonthlyCharts(incomes, expenses, year) {
    destroyChart("barChart", barChartInstance);
    barChartInstance = null;

    destroyChart("lineChart", lineChartInstance);
    lineChartInstance = null;

    const monthLabels = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    const incomeByMonth = Array(12).fill(0);
    const expenseByMonth = Array(12).fill(0);

    incomes.forEach(income => {
        const date = parseRecordDate(income, "income");

        if (date && date.getFullYear() === year) {
            incomeByMonth[date.getMonth()] +=
                getRecordAmount(income);
        }
    });

    expenses.forEach(expense => {
        const date = parseRecordDate(expense, "expense");

        if (date && date.getFullYear() === year) {
            expenseByMonth[date.getMonth()] +=
                getRecordAmount(expense);
        }
    });

    barChartInstance = new Chart(
        document.getElementById("barChart"),
        {
            type: "bar",
            data: {
                labels: monthLabels,
                datasets: [
                    {
                        label: "Income",
                        data: incomeByMonth
                    },
                    {
                        label: "Expense",
                        data: expenseByMonth
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: value => formatCurrency(value)
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label(context) {
                                return `${context.dataset.label}: ${
                                    formatCurrency(context.raw)
                                }`;
                            }
                        }
                    }
                }
            }
        }
    );

    lineChartInstance = new Chart(
        document.getElementById("lineChart"),
        {
            type: "line",
            data: {
                labels: monthLabels,
                datasets: [{
                    label: `Expenses in ${year}`,
                    data: expenseByMonth,
                    fill: false,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: value => formatCurrency(value)
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label(context) {
                                return formatCurrency(context.raw);
                            }
                        }
                    }
                }
            }
        }
    );
}

// ------------------------------
// Apply selected filters
// ------------------------------

function updateAnalytics() {
    const period = getSelectedPeriod();

    const filteredIncomes = filterRecordsByPeriod(
        allIncomes,
        "income",
        period
    );

    const filteredExpenses = filterRecordsByPeriod(
        allExpenses,
        "expense",
        period
    );

    updateSummary(filteredIncomes, filteredExpenses);
    renderExpenseChart(filteredExpenses);

    // Monthly charts always show all 12 months of the selected year.
    renderMonthlyCharts(allIncomes, allExpenses, period.year);
}

// ------------------------------
// Initialize analytics page
// ------------------------------

async function initializeAnalytics() {
    try {
        const [incomes, expenses] = await Promise.all([
            fetchAnalyticsData("/income"),
            fetchAnalyticsData("/expenses")
        ]);

        if (!Array.isArray(incomes) || !Array.isArray(expenses)) {
            throw new Error(
                "Income or expense API returned an unexpected response."
            );
        }

        allIncomes = incomes;
        allExpenses = expenses;

        initializeFilters();
        updateAnalytics();

    } catch (error) {
        console.error("Analytics loading failed:", error);

        document.getElementById("totalIncome").textContent =
            "Unavailable";

        document.getElementById("totalExpense").textContent =
            "Unavailable";

        document.getElementById("totalSavings").textContent =
            "Unavailable";

        document.getElementById("savingsRate").textContent =
            "Unavailable";
    }
}

initializeAnalytics();
