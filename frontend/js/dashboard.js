
checkAuth();

/* ==========================================
   SMART EXPENSE PLATFORM - DASHBOARD
   ========================================== */

const totalBalanceElement = document.getElementById("totalBalance");
const dashboardIncomeElement = document.getElementById("dashboardIncome");
const dashboardExpensesElement = document.getElementById("dashboardExpenses");
const dashboardBudgetLeftElement = document.getElementById("dashboardBudgetLeft");
const recentTransactionsBody = document.getElementById("recentTransactionsBody");
const dashboardMessage = document.getElementById("dashboardMessage");

let dashboardIncomes = [];
let dashboardExpenses = [];
let dashboardBudgets = [];

// Format amounts in Indian Rupees.
function formatCurrency(amount) {
    return Number(amount || 0).toLocaleString("en-IN", {
        style: "currency",
        currency: "INR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

// Format YYYY-MM-DD without shifting the date because of time zones.
function formatDashboardDate(dateValue) {
    if (!dateValue) return "-";

    const parts = String(dateValue).split("-");
    if (parts.length !== 3) return dateValue;

    const date = new Date(
        Number(parts[0]),
        Number(parts[1]) - 1,
        Number(parts[2])
    );

    return date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}

// Use local date values to avoid UTC month-boundary problems.
function getCurrentMonthAndYear() {
    const now = new Date();

    return {
        month: now.getMonth() + 1,
        year: now.getFullYear()
    };
}

// Checks whether a date belongs to the current month.
function isInCurrentMonth(dateValue, month, year) {
    if (!dateValue) return false;

    const parts = String(dateValue).split("-");
    if (parts.length !== 3) return false;

    return Number(parts[1]) === month &&
           Number(parts[0]) === year;
}

// Fetch an authenticated API endpoint.
async function fetchDashboardData(endpoint) {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    if (response.status === 401 || response.status === 403) {
        logout();
        throw new Error("Your session has expired. Please log in again.");
    }

    if (!response.ok) {
        throw new Error(
            `Unable to load ${endpoint} (HTTP ${response.status}).`
        );
    }

    return response.json();
}

// Load income, expenses and budgets from the backend.
async function loadDashboardData() {
    dashboardMessage.textContent = "Loading your financial data...";

    try {
        const results = await Promise.allSettled([
            fetchDashboardData("/income"),
            fetchDashboardData("/expenses"),
            fetchDashboardData("/budgets")
        ]);

        const [incomeResult, expenseResult, budgetResult] = results;

        const failedRequests = results.filter(
            result => result.status === "rejected"
        );

        if (failedRequests.length > 0) {
            console.error("Dashboard API errors:", failedRequests);
        }

        // Do not display a misleading balance if an essential
        // income or expense request fails.
        if (
            incomeResult.status === "rejected" ||
            expenseResult.status === "rejected"
        ) {
            throw new Error(
                "Income or expense data could not be loaded. Check the browser console."
            );
        }

        if (
            !Array.isArray(incomeResult.value) ||
            !Array.isArray(expenseResult.value)
        ) {
            throw new Error("The income or expense API returned an unexpected response.");
        }

        dashboardIncomes = incomeResult.value;
        dashboardExpenses = expenseResult.value;

        // Budget data is separate: retain the other cards if this request fails.
        dashboardBudgets =
            budgetResult.status === "fulfilled" &&
            Array.isArray(budgetResult.value)
                ? budgetResult.value
                : [];

        updateDashboardSummary();
        renderRecentTransactions();

        if (budgetResult.status === "rejected") {
            dashboardMessage.textContent =
                "Income and expenses loaded. Budget data is currently unavailable.";
        } else {
            dashboardMessage.textContent =
                "Your dashboard is up to date.";
        }

    } catch (error) {
        console.error("Dashboard loading error:", error);
        dashboardMessage.textContent =
            error.message || "Unable to load dashboard data.";

        recentTransactionsBody.innerHTML = `
            <tr>
                <td colspan="4" class="py-8 text-center text-red-600">
                    Unable to load transactions. Please refresh the page.
                </td>
            </tr>
        `;
    }
}

// Update summary cards.
function updateDashboardSummary() {
    const { month, year } = getCurrentMonthAndYear();

    // All-time balance: all income minus all expenses.
    const allTimeIncome = dashboardIncomes.reduce(
        (sum, income) => sum + Number(income.amount || 0),
        0
    );

    const allTimeExpenses = dashboardExpenses.reduce(
        (sum, expense) => sum + Number(expense.amount || 0),
        0
    );

    const allTimeBalance = allTimeIncome - allTimeExpenses;

    // Current-month income.
    const currentMonthIncome = dashboardIncomes
        .filter(income =>
            isInCurrentMonth(income.incomeDate, month, year)
        )
        .reduce(
            (sum, income) => sum + Number(income.amount || 0),
            0
        );

    // Current-month expenses.
    const currentMonthExpenses = dashboardExpenses
        .filter(expense =>
            isInCurrentMonth(expense.expenseDate, month, year)
        )
        .reduce(
            (sum, expense) => sum + Number(expense.amount || 0),
            0
        );

    // Current-month budget minus current-month expenses.
    const currentMonthBudgets = dashboardBudgets.filter(budget =>
        Number(budget.month) === month &&
        Number(budget.year) === year
    );

    const currentMonthBudgetTotal = currentMonthBudgets.reduce(
        (sum, budget) => sum + Number(budget.budgetAmount || 0),
        0
    );

    const budgetLeft = currentMonthBudgetTotal - currentMonthExpenses;

    // Update displayed values.
    totalBalanceElement.textContent = formatCurrency(allTimeBalance);
    dashboardIncomeElement.textContent = formatCurrency(currentMonthIncome);
    dashboardExpensesElement.textContent = formatCurrency(currentMonthExpenses);
    dashboardBudgetLeftElement.textContent = formatCurrency(budgetLeft);

    // Update the balance color.
    totalBalanceElement.classList.toggle(
        "text-red-600",
        allTimeBalance < 0
    );
    totalBalanceElement.classList.toggle(
        "text-green-600",
        allTimeBalance >= 0
    );

    // Update the budget remaining color.
    dashboardBudgetLeftElement.classList.toggle(
        "text-red-600",
        budgetLeft < 0
    );
    dashboardBudgetLeftElement.classList.toggle(
        "text-orange-500",
        budgetLeft >= 0
    );
}

// Render the latest five income and expense transactions.
function renderRecentTransactions() {
    recentTransactionsBody.innerHTML = "";

    const transactions = [
        ...dashboardIncomes.map(income => ({
            title: income.sourceName || "Income",
            category: income.incomeCategory || "Income",
            amount: Number(income.amount || 0),
            date: income.incomeDate,
            type: "income"
        })),

        ...dashboardExpenses.map(expense => ({
            title:
                expense.title ||
                expense.expenseName ||
                expense.description ||
                "Expense",
            category: expense.expenseCategory || "Expense",
            amount: Number(expense.amount || 0),
            date: expense.expenseDate,
            type: "expense"
        }))
    ];

    transactions.sort((a, b) =>
        String(b.date || "").localeCompare(String(a.date || ""))
    );

    const recentTransactions = transactions.slice(0, 5);

    if (recentTransactions.length === 0) {
        recentTransactionsBody.innerHTML = `
            <tr>
                <td colspan="4" class="py-8 text-center text-slate-500">
                    No transactions yet. Add your first income or expense!
                </td>
            </tr>
        `;
        return;
    }

    recentTransactions.forEach(transaction => {
        const row = document.createElement("tr");
        row.className = "border-b last:border-0 hover:bg-slate-50";

        const titleCell = document.createElement("td");
        titleCell.className = "py-4 pr-4 font-medium";
        titleCell.textContent = transaction.title;

        const categoryCell = document.createElement("td");
        categoryCell.className = "pr-4 text-slate-600";
        categoryCell.textContent = transaction.category;

        const amountCell = document.createElement("td");
        amountCell.className =
            transaction.type === "income"
                ? "pr-4 font-semibold text-green-600"
                : "pr-4 font-semibold text-red-600";

        amountCell.textContent =
            (transaction.type === "income" ? "+ " : "- ") +
            formatCurrency(transaction.amount);

        const dateCell = document.createElement("td");
        dateCell.className = "text-slate-600";
        dateCell.textContent = formatDashboardDate(transaction.date);

        row.append(titleCell, categoryCell, amountCell, dateCell);
        recentTransactionsBody.appendChild(row);
    });
}

// Initialize dashboard.
loadDashboardData();
