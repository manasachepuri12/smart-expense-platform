
checkAuth();

let allIncomes = [];
let allExpenses = [];
let allBudgets = [];

let displayedReportRows = [];

const reportMonthNames = [
    "January", "February", "March", "April",
    "May", "June", "July", "August",
    "September", "October", "November", "December"
];

// ------------------------------
// Helpers
// ------------------------------

function formatReportCurrency(amount) {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(Number(amount) || 0);
}

async function fetchReportData(endpoint) {
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

function getReportAmount(record) {
    return Number(record.amount) || 0;
}

function getReportDate(record, type) {
    const dateString = type === "income"
        ? record.incomeDate
        : record.expenseDate;

    if (!dateString) {
        return null;
    }

    // Prevent timezone shifts for date-only values.
    const date = new Date(`${dateString}T00:00:00`);

    return Number.isNaN(date.getTime()) ? null : date;
}

function getReportYear() {
    return Number(document.getElementById("reportYear").value);
}

function getYearRecords(records, type, year) {
    return records.filter(record => {
        const date = getReportDate(record, type);
        return date && date.getFullYear() === year;
    });
}

function setReportMessage(message) {
    document.getElementById("monthlyReportBody").innerHTML = `
        <tr>
            <td colspan="5"
                class="py-8 text-center text-slate-500">
                ${message}
            </td>
        </tr>
    `;
}

// ------------------------------
// Initialize year filter
// ------------------------------


function initializeReportYearFilter() {
    const select = document.getElementById("reportYear");
    const currentYear = new Date().getFullYear();

    // Show the current year and the previous 5 years.
    const years = new Set();

    for (let year = currentYear; year >= currentYear - 5; year--) {
        years.add(year);
    }

    // Also include any years found in transaction history.
    allIncomes.forEach(income => {
        const date = getReportDate(income, "income");
        if (date) years.add(date.getFullYear());
    });

    allExpenses.forEach(expense => {
        const date = getReportDate(expense, "expense");
        if (date) years.add(date.getFullYear());
    });

    allBudgets.forEach(budget => {
        const year = Number(budget.year);

        if (Number.isInteger(year) && year > 0) {
            years.add(year);
        }
    });

    select.innerHTML = "";

    [...years]
        .sort((a, b) => b - a)
        .forEach(year => {
            const option = document.createElement("option");
            option.value = String(year);
            option.textContent = String(year);
            select.appendChild(option);
        });

    select.value = String(currentYear);
}


// ------------------------------
// Summary cards
// ------------------------------

function updateReportSummary(incomes, expenses, budgets) {
    const totalIncome = incomes.reduce(
        (sum, item) => sum + getReportAmount(item),
        0
    );

    const totalExpenses = expenses.reduce(
        (sum, item) => sum + getReportAmount(item),
        0
    );

    const totalBudget = budgets.reduce(
        (sum, item) => sum + (Number(item.budgetAmount) || 0),
        0
    );

    const netSavings = totalIncome - totalExpenses;

    const budgetUtilized = totalBudget > 0
        ? (totalExpenses / totalBudget) * 100
        : null;

    document.getElementById("reportTotalIncome").textContent =
        formatReportCurrency(totalIncome);

    document.getElementById("reportTotalExpenses").textContent =
        formatReportCurrency(totalExpenses);

    document.getElementById("reportNetSavings").textContent =
        formatReportCurrency(netSavings);

    document.getElementById("reportBudgetUtilized").textContent =
        budgetUtilized === null
            ? "N/A"
            : `${budgetUtilized.toFixed(1)}%`;
}

// ------------------------------
// Monthly report table
// ------------------------------

function renderMonthlyReport(year) {
    const monthlyData = reportMonthNames.map((month, index) => ({
        month,
        monthIndex: index,
        income: 0,
        expenses: 0
    }));

    getYearRecords(allIncomes, "income", year).forEach(income => {
        const date = getReportDate(income, "income");
        monthlyData[date.getMonth()].income += getReportAmount(income);
    });

    getYearRecords(allExpenses, "expense", year).forEach(expense => {
        const date = getReportDate(expense, "expense");
        monthlyData[date.getMonth()].expenses += getReportAmount(expense);
    });

    displayedReportRows = monthlyData.map(item => {
        const savings = item.income - item.expenses;

        let status = "No Activity";
        let statusClass = "bg-slate-100 text-slate-600";

        if (item.income > 0 || item.expenses > 0) {
            if (savings > 0) {
                status = "Surplus";
                statusClass = "bg-green-100 text-green-700";
            } else if (savings < 0) {
                status = "Deficit";
                statusClass = "bg-red-100 text-red-700";
            } else {
                status = "Break-even";
                statusClass = "bg-yellow-100 text-yellow-700";
            }
        }

        return {
            month: item.month,
            income: item.income,
            expenses: item.expenses,
            savings,
            status
        };
    });

    const tbody = document.getElementById("monthlyReportBody");

    tbody.innerHTML = displayedReportRows.map(item => {
        const statusClass = item.status === "Surplus"
            ? "bg-green-100 text-green-700"
            : item.status === "Deficit"
                ? "bg-red-100 text-red-700"
                : item.status === "Break-even"
                    ? "bg-yellow-100 text-yellow-700"
                    : "bg-slate-100 text-slate-600";

        return `
            <tr class="border-b border-slate-100 hover:bg-slate-50">
                <td class="py-4 font-medium">${item.month}</td>

                <td class="text-green-600">
                    ${formatReportCurrency(item.income)}
                </td>

                <td class="text-red-600">
                    ${formatReportCurrency(item.expenses)}
                </td>

                <td class="${item.savings < 0 ? "text-red-600" : "text-blue-700"}">
                    ${formatReportCurrency(item.savings)}
                </td>

                <td>
                    <span class="${statusClass} px-3 py-1 rounded-full text-sm">
                        ${item.status}
                    </span>
                </td>
            </tr>
        `;
    }).join("");
}

// ------------------------------
// Apply selected year
// ------------------------------

function updateReports() {
    const year = getReportYear();

    const incomes = getYearRecords(allIncomes, "income", year);
    const expenses = getYearRecords(allExpenses, "expense", year);

    const budgets = allBudgets.filter(
        budget => Number(budget.year) === year
    );

    updateReportSummary(incomes, expenses, budgets);
    renderMonthlyReport(year);
}

// ------------------------------
// Export CSV
// ------------------------------

function exportReportsCSV() {
    if (displayedReportRows.length === 0) {
        alert("There is no report data to export.");
        return;
    }

    const headers = [
        "Month",
        "Income (INR)",
        "Expenses (INR)",
        "Net Savings (INR)",
        "Status"
    ];

    const rows = displayedReportRows.map(item => [
        item.month,
        item.income.toFixed(2),
        item.expenses.toFixed(2),
        item.savings.toFixed(2),
        item.status
    ]);

    const csv = [headers, ...rows]
        .map(row => row.map(value => {
            const safeValue = String(value).replace(/"/g, '""');
            return `"${safeValue}"`;
        }).join(","))
        .join("\r\n");

    const blob = new Blob(
        ["\uFEFF" + csv],
        { type: "text/csv;charset=utf-8;" }
    );

    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = url;
    link.download = `financial-report-${getReportYear()}.csv`;

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}

// ------------------------------
// Print / save as PDF
// ------------------------------

function downloadReportPDF() {
    // In the print dialog, choose "Save as PDF".
    window.print();
}

// ------------------------------
// Initialize Reports
// ------------------------------

async function initializeReports() {
    try {
        const [incomes, expenses, budgets] = await Promise.all([
            fetchReportData("/income"),
            fetchReportData("/expenses"),
            fetchReportData("/budgets")
        ]);

        if (
            !Array.isArray(incomes) ||
            !Array.isArray(expenses) ||
            !Array.isArray(budgets)
        ) {
            throw new Error("Unexpected response from a report API.");
        }

        allIncomes = incomes;
        allExpenses = expenses;
        allBudgets = budgets;

        initializeReportYearFilter();
        updateReports();

        document
            .getElementById("applyReportFilter")
            .addEventListener("click", updateReports);

        document
            .getElementById("exportCsvBtn")
            .addEventListener("click", exportReportsCSV);

        document
            .getElementById("downloadPdfBtn")
            .addEventListener("click", downloadReportPDF);

    } catch (error) {
        console.error("Failed to load reports:", error);

        document.getElementById("reportTotalIncome").textContent =
            "Unavailable";

        document.getElementById("reportTotalExpenses").textContent =
            "Unavailable";

        document.getElementById("reportNetSavings").textContent =
            "Unavailable";

        document.getElementById("reportBudgetUtilized").textContent =
            "Unavailable";

        setReportMessage(
            "Unable to load reports. Check your login and backend APIs, then refresh."
        );
    }
}

initializeReports();
