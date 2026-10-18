
checkAuth();

const addBudgetBtn = document.getElementById("addBudgetBtn");
const budgetTableBody = document.getElementById("budgetTableBody");
const budgetProgress = document.getElementById("budgetProgress");
let budgets = [];
let expenseCategories = [];
let budgetAnalytics = [];
let editingBudgetId = null;

const currentDate = new Date();

function formatCurrency(amount) {
    return Number(amount || 0).toLocaleString("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 2
    });
}

function getMonthName(month) {
    return new Date(2000, month - 1, 1).toLocaleString("en-IN", {
        month: "long"
    });
}

async function apiRequest(url, options = {}) {
    const response = await fetch(`${BASE_URL}${url}`, {
        ...options,
        headers: {
            Authorization: `Bearer ${getToken()}`,
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...options.headers
        }
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Request failed (${response.status})`);
    }

    if (response.status === 204) return null;

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

// Create the Add/Edit Budget modal.
const modal = document.createElement("div");
modal.id = "budgetModal";
modal.className =
    "hidden fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4";

modal.innerHTML = `
    <div class="bg-white w-full max-w-lg rounded-2xl shadow-xl p-8">
        <div class="flex justify-between items-center mb-6">
            <h2 id="budgetModalTitle" class="text-2xl font-bold text-slate-800">
                Add Budget
            </h2>
            <button type="button" id="closeBudgetModal"
                class="text-2xl text-slate-500 hover:text-red-600"
                aria-label="Close">&times;</button>
        </div>

        <form id="budgetForm" class="space-y-5">
            <div>
                <label for="budgetCategory" class="block mb-2 font-semibold">
                    Expense Category
                </label>
                <select id="budgetCategory" required
                    class="w-full rounded-xl border border-gray-300 px-4 py-3">
                    <option value="">Select Category</option>
                </select>
            </div>

            <div>
                <label for="budgetAmount" class="block mb-2 font-semibold">
                    Budget Amount (₹)
                </label>
                <input id="budgetAmount" type="number" min="0.01"
                    step="0.01" required placeholder="Enter budget amount"
                    class="w-full rounded-xl border border-gray-300 px-4 py-3">
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label for="budgetMonth" class="block mb-2 font-semibold">
                        Month
                    </label>
                    <select id="budgetMonth" required
                        class="w-full rounded-xl border border-gray-300 px-4 py-3">
                        ${Array.from({ length: 12 }, (_, i) =>
                            `<option value="${i + 1}" ${i + 1 === currentDate.getMonth() + 1 ? "selected" : ""}>
                                ${getMonthName(i + 1)}
                            </option>`
                        ).join("")}
                    </select>
                </div>

                <div>
                    <label for="budgetYear" class="block mb-2 font-semibold">
                        Year
                    </label>
                    <input id="budgetYear" type="number" min="2000" max="2100"
                        required value="${currentDate.getFullYear()}"
                        class="w-full rounded-xl border border-gray-300 px-4 py-3">
                </div>
            </div>

            <div class="flex justify-end gap-3">
                <button type="button" id="cancelBudgetBtn"
                    class="px-5 py-3 rounded-xl border hover:bg-gray-100">
                    Cancel
                </button>
                <button type="submit" id="saveBudgetBtn"
                    class="btn-primary px-6 py-3 rounded-xl">
                    Save Budget
                </button>
            </div>
        </form>
    </div>
`;

document.body.appendChild(modal);

const budgetForm = document.getElementById("budgetForm");

function closeBudgetModal() {
    modal.classList.add("hidden");
    budgetForm.reset();
    editingBudgetId = null;

    document.getElementById("budgetModalTitle").textContent = "Add Budget";
    document.getElementById("saveBudgetBtn").textContent = "Save Budget";
    document.getElementById("budgetMonth").value =
        String(currentDate.getMonth() + 1);
    document.getElementById("budgetYear").value =
        String(currentDate.getFullYear());
}

document.getElementById("closeBudgetModal")
    .addEventListener("click", closeBudgetModal);
document.getElementById("cancelBudgetBtn")
    .addEventListener("click", closeBudgetModal);

modal.addEventListener("click", event => {
    if (event.target === modal) closeBudgetModal();
});

document.addEventListener("keydown", event => {
    if (event.key === "Escape" && !modal.classList.contains("hidden")) {
        closeBudgetModal();
    }
});

async function loadExpenseCategories() {
    expenseCategories = await apiRequest("/expense-categories");

    const select = document.getElementById("budgetCategory");
    select.innerHTML = '<option value="">Select Category</option>';

    expenseCategories.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.textContent = category.name;
        select.appendChild(option);
    });
}

async function openBudgetModal(budget = null) {
    try {
        await loadExpenseCategories();
        closeBudgetModal();

        if (budget) {
            editingBudgetId = budget.id;
            document.getElementById("budgetModalTitle").textContent = "Edit Budget";
            document.getElementById("saveBudgetBtn").textContent = "Update Budget";
            document.getElementById("budgetCategory").value =
                String(expenseCategories.find(
                    category => category.name === budget.expenseCategory
                )?.id || "");
            document.getElementById("budgetAmount").value = budget.budgetAmount;
            document.getElementById("budgetMonth").value = String(budget.month);
            document.getElementById("budgetYear").value = String(budget.year);
        }

        modal.classList.remove("hidden");
    } catch (error) {
        console.error("Unable to open budget form:", error);
        alert("Unable to load expense categories. Check the Console for details.");
    }
}

addBudgetBtn.addEventListener("click", () => openBudgetModal());

budgetForm.addEventListener("submit", async event => {
    event.preventDefault();

    const request = {
        budgetAmount: Number(document.getElementById("budgetAmount").value),
        month: Number(document.getElementById("budgetMonth").value),
        year: Number(document.getElementById("budgetYear").value),
        expenseCategoryId: Number(document.getElementById("budgetCategory").value)
    };

    if (
        !Number.isFinite(request.budgetAmount) ||
        request.budgetAmount <= 0 ||
        !request.expenseCategoryId ||
        request.year < 2000 ||
        request.year > 2100
    ) {
        alert("Please enter valid budget details.");
        return;
    }

    try {
        const url = editingBudgetId
            ? `/budgets/${editingBudgetId}`
            : "/budgets";

        await apiRequest(url, {
            method: editingBudgetId ? "PUT" : "POST",
            body: JSON.stringify(request)
        });

        alert(editingBudgetId
            ? "Budget updated successfully!"
            : "Budget created successfully!");

        closeBudgetModal();
        await loadBudgets();
    } catch (error) {
        console.error("Budget save error:", error);
        alert("Unable to save budget. " + error.message);
    }
});


function renderBudgets() {
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();

    const currentBudgets = budgets.filter(
        budget =>
            Number(budget.month) === month &&
            Number(budget.year) === year
    );

    const totalBudget = currentBudgets.reduce(
        (sum, budget) => sum + Number(budget.budgetAmount || 0),
        0
    );

    const totalSpent = budgetAnalytics.reduce(
        (sum, item) => sum + Number(item.spent || 0),
        0
    );

    const totalRemaining = budgetAnalytics.reduce(
        (sum, item) => sum + Number(item.remaining || 0),
        0
    );

    document.getElementById("monthlyBudget").textContent =
        formatCurrency(totalBudget);

    document.getElementById("totalSpent").textContent =
        formatCurrency(totalSpent);

    document.getElementById("remainingBudget").textContent =
        formatCurrency(totalRemaining);

    budgetTableBody.innerHTML = "";

    if (budgets.length === 0) {
        budgetTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="py-8 text-center text-slate-500">
                    No budgets found. Click "Add Budget" to create one.
                </td>
            </tr>`;
    } else {
        budgets.forEach(budget => {
            const row = document.createElement("tr");
            row.className = "border-b hover:bg-slate-50";

            const values = [
                budget.expenseCategory || "-",
                formatCurrency(budget.budgetAmount),
                getMonthName(Number(budget.month)),
                budget.year
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.className = "py-4";
                cell.textContent = value;
                row.appendChild(cell);
            });

            const actions = document.createElement("td");
            actions.className = "py-4 whitespace-nowrap space-x-3";

            const edit = document.createElement("button");
            edit.type = "button";
            edit.className = "text-blue-600 hover:text-blue-800";
            edit.title = "Edit budget";
            edit.innerHTML = '<i class="fa-solid fa-pen"></i>';
            edit.addEventListener("click", () => openBudgetModal(budget));

            const remove = document.createElement("button");
            remove.type = "button";
            remove.className = "text-red-600 hover:text-red-800";
            remove.title = "Delete budget";
            remove.innerHTML = '<i class="fa-solid fa-trash"></i>';
            remove.addEventListener("click", () => deleteBudget(budget.id));

            actions.append(edit, remove);
            row.appendChild(actions);
            budgetTableBody.appendChild(row);
        });
    }

    renderBudgetProgress();
}



function renderBudgetProgress() {
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();

    budgetProgress.innerHTML = "";

    if (budgetAnalytics.length === 0) {
        budgetProgress.innerHTML = `
            <p class="text-slate-500">
                No budgets for ${getMonthName(month)} ${year}.
                Add a budget for this month to see its progress.
            </p>`;
        return;
    }

    budgetAnalytics.forEach(item => {
        const percentage = Math.max(
            0,
            Number(item.percentageUsed || 0)
        );

        // Allow the bar to exceed 100% when spending exceeds the budget.
        const barWidth = Math.min(percentage, 100);

        const exceeded = item.status === "Exceeded";

        const container = document.createElement("div");
        container.className = "mb-6";

        const header = document.createElement("div");
        header.className =
            "flex flex-wrap justify-between gap-2 mb-2";

        const name = document.createElement("span");
        name.className = "font-semibold text-slate-800";
        name.textContent = item.category;

        const amounts = document.createElement("span");
        amounts.className = "text-slate-600";
        amounts.textContent =
            `${formatCurrency(item.spent)} spent of ${formatCurrency(item.budget)}`;

        header.append(name, amounts);

        const track = document.createElement("div");
        track.className = "w-full bg-gray-200 rounded-full h-3";

        const bar = document.createElement("div");
        bar.className =
            `${exceeded ? "bg-red-500" : "bg-blue-500"} h-3 rounded-full transition-all`;
        bar.style.width = `${barWidth}%`;
        bar.title = `${percentage.toFixed(2)}% used`;

        track.appendChild(bar);

        const footer = document.createElement("div");
        footer.className =
            "flex flex-wrap justify-between gap-2 mt-2 text-sm";

        const remaining = document.createElement("span");
        remaining.className =
            Number(item.remaining) < 0
                ? "text-red-600"
                : "text-green-600";

        remaining.textContent =
            `${formatCurrency(item.remaining)} remaining`;

        const status = document.createElement("span");
        status.className = exceeded
            ? "text-red-600 font-semibold"
            : "text-green-600 font-semibold";

        status.textContent =
            `${item.status} · ${percentage.toFixed(1)}% used`;

        footer.append(remaining, status);
        container.append(header, track, footer);
        budgetProgress.appendChild(container);
    });
}


async function deleteBudget(id) {
    if (!confirm("Are you sure you want to delete this budget?")) return;

    try {
        await apiRequest(`/budgets/${id}`, { method: "DELETE" });
        alert("Budget deleted successfully!");
        await loadBudgets();
    } catch (error) {
        console.error("Budget deletion error:", error);
        alert("Unable to delete budget. " + error.message);
    }
}

async function loadBudgets() {
    try {
        const month = currentDate.getMonth() + 1;
        const year = currentDate.getFullYear();

        // Load all budgets for the logged-in user.
        budgets = await apiRequest("/budgets");

        // Load spending analytics for the current month.
        budgetAnalytics = await apiRequest(
            `/analytics/budget/monthly?month=${month}&year=${year}`
        );

        renderBudgets();
    } catch (error) {
        console.error("Budget loading error:", error);

        budgetAnalytics = [];

        document.getElementById("monthlyBudget").textContent =
            "Unavailable";

        document.getElementById("totalSpent").textContent =
            "Unavailable";

        document.getElementById("remainingBudget").textContent =
            "Unavailable";

        budgetTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="py-8 text-center text-red-600">
                    Unable to load budgets or analytics.
                    Check the browser Console and Network tab.
                </td>
            </tr>`;

        budgetProgress.textContent =
            "Unable to load budget analytics.";
    }
}


async function initializeBudgetPage() {
    await loadBudgets();
}

initializeBudgetPage();
