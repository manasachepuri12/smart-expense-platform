
checkAuth();

const addIncomeBtn = document.getElementById("addIncomeBtn");
const incomeTableBody = document.getElementById("incomeTableBody");
const incomeSearch = document.getElementById("incomeSearch");
const categoryFilter = document.getElementById("categoryFilter");

let incomes = [];
let incomeCategories = [];

// Create the Add/Edit Income modal
const modal = document.createElement("div");
modal.id = "incomeModal";
modal.className =
    "hidden fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4";

modal.innerHTML = `
    <div class="bg-white w-full max-w-xl rounded-2xl shadow-xl p-8 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-2xl font-bold text-slate-800">Add New Income</h2>
            <button type="button" id="closeIncomeModal"
                class="text-slate-500 hover:text-red-600 text-2xl"
                aria-label="Close">&times;</button>
        </div>

        <form id="incomeForm">
            <div class="mb-5">
                <label for="sourceName" class="block mb-2 font-semibold">Income Source</label>
                <input id="sourceName" type="text" maxlength="100" required
                    placeholder="Example: Monthly Salary"
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <div class="mb-5">
                <label for="incomeCategory" class="block mb-2 font-semibold">Category</label>
                <select id="incomeCategory" required
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="">Select Category</option>
                </select>
            </div>

            <div class="mb-5">
                <label for="incomeAmount" class="block mb-2 font-semibold">Amount (₹)</label>
                <input id="incomeAmount" type="number" min="0.01" step="0.01" required
                    placeholder="₹ 0.00"
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <div class="mb-5">
                <label for="incomeDate" class="block mb-2 font-semibold">Income Date</label>
                <input id="incomeDate" type="date" required
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <div class="mb-6">
                <label for="incomeDescription" class="block mb-2 font-semibold">Description</label>
                <textarea id="incomeDescription" maxlength="2000" rows="3"
                    placeholder="Add details about this income..."
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
            </div>

            <div class="flex justify-end gap-3">
                <button type="button" id="cancelIncomeBtn"
                    class="px-5 py-3 rounded-xl border border-gray-300 hover:bg-gray-100">
                    Cancel
                </button>
                <button type="submit" id="saveIncomeBtn"
                    class="btn-primary px-6 py-3 rounded-xl">
                    <i class="fa-solid fa-plus mr-2"></i> Add Income
                </button>
            </div>
        </form>
    </div>
`;

document.body.appendChild(modal);

const incomeForm = document.getElementById("incomeForm");

function closeModal() {
    modal.classList.add("hidden");
    incomeForm.reset();
    delete incomeForm.dataset.editingId;

    document.querySelector("#incomeModal h2").textContent = "Add New Income";
    document.getElementById("saveIncomeBtn").innerHTML =
        '<i class="fa-solid fa-plus mr-2"></i> Add Income';
}

addIncomeBtn.addEventListener("click", async () => {
    closeModal();

    try {
        await loadCategories();
        modal.classList.remove("hidden");
    } catch (error) {
        console.error(error);
        alert("Unable to load income categories. Please try again.");
    }
});

document.getElementById("closeIncomeModal").addEventListener("click", closeModal);
document.getElementById("cancelIncomeBtn").addEventListener("click", closeModal);

modal.addEventListener("click", (event) => {
    if (event.target === modal) closeModal();
});

document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && !modal.classList.contains("hidden")) {
        closeModal();
    }
});

// Load categories from the backend
async function loadCategories() {
    const response = await fetch(`${BASE_URL}/income-categories`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    if (!response.ok) {
        throw new Error("Failed to load income categories");
    }

    incomeCategories = await response.json();

    const selects = [
        document.getElementById("incomeCategory"),
        categoryFilter
    ];

    selects.forEach((select, index) => {
        const previousValue = select.value;
        select.innerHTML = index === 0
            ? '<option value="">Select Category</option>'
            : '<option value="">All Categories</option>';

        incomeCategories.forEach((category) => {
            const option = document.createElement("option");
            option.value = index === 0 ? category.id : category.name;
            option.textContent = category.name;
            select.appendChild(option);
        });

        if ([...select.options].some(option => option.value === previousValue)) {
            select.value = previousValue;
        }
    });
}

// Submit new income or update an existing record
incomeForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const editingId = incomeForm.dataset.editingId;

    const incomeData = {
        sourceName: document.getElementById("sourceName").value.trim(),
        incomeCategoryId: Number(document.getElementById("incomeCategory").value),
        amount: Number(document.getElementById("incomeAmount").value),
        incomeDate: document.getElementById("incomeDate").value,
        description: document.getElementById("incomeDescription").value.trim()
    };

    if (!incomeData.sourceName || !incomeData.incomeCategoryId ||
        !incomeData.incomeDate || !Number.isFinite(incomeData.amount) ||
        incomeData.amount <= 0) {
        alert("Please enter valid income details.");
        return;
    }

    const url = editingId
        ? `${BASE_URL}/income/${editingId}`
        : `${BASE_URL}/income`;

    try {
        const response = await fetch(url, {
            method: editingId ? "PUT" : "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${getToken()}`
            },
            body: JSON.stringify(incomeData)
        });

        const responseText = await response.text();
        let result = null;

        if (responseText) {
            try {
                result = JSON.parse(responseText);
            } catch {
                result = { message: responseText };
            }
        }

        if (!response.ok) {
            throw new Error(result?.message || "Unable to save income.");
        }

        alert(editingId
            ? "Income updated successfully!"
            : "Income added successfully!");

        closeModal();
        await loadIncomes();
    } catch (error) {
        console.error("Income save error:", error);
        alert(error.message || "Unable to connect to the server.");
    }
});

// Load income history
async function loadIncomes() {
    try {
        const response = await fetch(`${BASE_URL}/income`, {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load income history");
        }

        incomes = await response.json();
        renderIncomes();
    } catch (error) {
        console.error("Income loading error:", error);
        incomeTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="py-8 text-center text-red-600">
                    Unable to load income records. Please refresh the page.
                </td>
            </tr>`;
    }
}

// Render table rows safely
function renderIncomes() {
    const searchText = incomeSearch.value.trim().toLowerCase();
    const selectedCategory = categoryFilter.value;

    const filteredIncomes = incomes.filter((income) => {
        const matchesSearch =
            (income.sourceName || "").toLowerCase().includes(searchText) ||
            (income.description || "").toLowerCase().includes(searchText) ||
            (income.incomeCategory || "").toLowerCase().includes(searchText);

        const matchesCategory =
            !selectedCategory || income.incomeCategory === selectedCategory;

        return matchesSearch && matchesCategory;
    });

    incomeTableBody.innerHTML = "";

    if (filteredIncomes.length === 0) {
        incomeTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="py-8 text-center text-slate-500">
                    No income records found.
                </td>
            </tr>`;
        return;
    }

    filteredIncomes.forEach((income) => {
        const row = document.createElement("tr");
        row.className = "border-b hover:bg-slate-50";

        const sourceCell = document.createElement("td");
        sourceCell.className = "py-4";
        sourceCell.textContent = income.sourceName || "-";
        sourceCell.title = income.description || "";

        const categoryCell = document.createElement("td");
        categoryCell.textContent = income.incomeCategory || "-";

        const amountCell = document.createElement("td");
        amountCell.className = "text-green-600 font-semibold";
        amountCell.textContent = Number(income.amount).toLocaleString("en-IN", {
            style: "currency",
            currency: "INR"
        });

        const dateCell = document.createElement("td");
        dateCell.textContent = formatIncomeDate(income.incomeDate);

        const actionsCell = document.createElement("td");
        actionsCell.className = "space-x-3 whitespace-nowrap";

        const editButton = document.createElement("button");
        editButton.type = "button";
        editButton.className = "text-blue-600 hover:text-blue-800";
        editButton.title = "Edit income";
        editButton.innerHTML = '<i class="fa-solid fa-pen"></i>';
        editButton.addEventListener("click", () => editIncome(income.id));

        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.className = "text-red-600 hover:text-red-800";
        deleteButton.title = "Delete income";
        deleteButton.innerHTML = '<i class="fa-solid fa-trash"></i>';
        deleteButton.addEventListener("click", () => deleteIncome(income.id));

        actionsCell.append(editButton, deleteButton);
        row.append(sourceCell, categoryCell, amountCell, dateCell, actionsCell);
        incomeTableBody.appendChild(row);
    });
}

function formatIncomeDate(dateValue) {
    if (!dateValue) return "-";

    const parts = dateValue.split("-");
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

// Edit an income record
async function editIncome(id) {
    try {
        await loadCategories();

        const response = await fetch(`${BASE_URL}/income/${id}`, {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        });

        if (!response.ok) {
            throw new Error("Unable to load this income record.");
        }

        const income = await response.json();

        incomeForm.dataset.editingId = id;
        document.getElementById("sourceName").value = income.sourceName || "";
        document.getElementById("incomeAmount").value = income.amount ?? "";
        document.getElementById("incomeDate").value = income.incomeDate || "";
        document.getElementById("incomeDescription").value = income.description || "";

        const matchingCategory = incomeCategories.find(
            category => category.name === income.incomeCategory
        );

        if (!matchingCategory) {
            throw new Error("The category for this income record could not be found.");
        }

        document.getElementById("incomeCategory").value = matchingCategory.id;

        document.querySelector("#incomeModal h2").textContent = "Update Income";
        document.getElementById("saveIncomeBtn").innerHTML =
            '<i class="fa-solid fa-floppy-disk mr-2"></i> Update Income';

        modal.classList.remove("hidden");
    } catch (error) {
        console.error("Income edit error:", error);
        alert(error.message || "Unable to edit income.");
    }
}

// Delete an income record
async function deleteIncome(id) {
    if (!confirm("Are you sure you want to delete this income record?")) {
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/income/${id}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        });

        if (!response.ok) {
            const text = await response.text();
            let message = text;

            try {
                message = JSON.parse(text).message || text;
            } catch {
                // Keep the response text if it isn't JSON.
            }

            throw new Error(message || "Unable to delete income.");
        }

        alert("Income deleted successfully!");
        await loadIncomes();
    } catch (error) {
        console.error("Income deletion error:", error);
        alert(error.message || "Unable to connect to the server.");
    }
}

incomeSearch.addEventListener("input", renderIncomes);
categoryFilter.addEventListener("change", renderIncomes);

// Initialize the page
async function initializeIncomePage() {
    try {
        await loadCategories();
    } catch (error) {
        console.error("Category loading error:", error);
        alert("Unable to load income categories. Please check the server.");
    }

    await loadIncomes();
}

initializeIncomePage();

/* ==========================================
   CUSTOM INCOME CATEGORY MANAGEMENT
   ========================================== */

const manageCategoriesBtn = document.getElementById("manageCategoriesBtn");
const categoryManager = document.getElementById("categoryManager");
const closeCategoryManagerBtn = document.getElementById("closeCategoryManager");
const incomeCategoryForm = document.getElementById("incomeCategoryForm");
const customIncomeCategoryName = document.getElementById("customIncomeCategoryName");
const saveIncomeCategoryBtn = document.getElementById("saveIncomeCategoryBtn");
const cancelIncomeCategoryEditBtn = document.getElementById("cancelIncomeCategoryEditBtn");
const incomeCategoryMessage = document.getElementById("incomeCategoryMessage");
const incomeCategoryTableBody = document.getElementById("incomeCategoryTableBody");

let managedIncomeCategories = [];

function showIncomeCategoryMessage(message, type = "success") {
    incomeCategoryMessage.textContent = message;
    incomeCategoryMessage.className =
        `text-sm mb-4 ${type === "error" ? "text-red-600" : "text-green-600"}`;
}

function resetIncomeCategoryForm() {
    incomeCategoryForm.reset();
    delete incomeCategoryForm.dataset.editingId;

    saveIncomeCategoryBtn.innerHTML =
        '<i class="fa-solid fa-plus mr-2"></i> Add Category';

    cancelIncomeCategoryEditBtn.classList.add("hidden");
}

async function incomeCategoryApiRequest(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: {
            Authorization: `Bearer ${getToken()}`,
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...options.headers
        }
    });

    const responseText = await response.text();
    let result = null;

    if (responseText) {
        try {
            result = JSON.parse(responseText);
        } catch {
            result = { message: responseText };
        }
    }

    if (!response.ok) {
        throw new Error(
            result?.message ||
            result?.error ||
            `Request failed (${response.status})`
        );
    }

    return result;
}

async function loadManagedIncomeCategories() {
    managedIncomeCategories = await incomeCategoryApiRequest(
        `${BASE_URL}/income-categories`
    );

    renderManagedIncomeCategories();
}

function renderManagedIncomeCategories() {
    incomeCategoryTableBody.innerHTML = "";

    if (!managedIncomeCategories.length) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");

        cell.colSpan = 3;
        cell.className = "py-6 text-center text-slate-500";
        cell.textContent = "No income categories found.";

        row.appendChild(cell);
        incomeCategoryTableBody.appendChild(row);
        return;
    }

    managedIncomeCategories.forEach(category => {
        const row = document.createElement("tr");
        row.className = "border-b hover:bg-slate-50";

        const nameCell = document.createElement("td");
        nameCell.className = "py-4 font-medium";
        nameCell.textContent = category.name;

        const typeCell = document.createElement("td");
        typeCell.className = "py-4";

        const isDefault = category.isDefault === true;
        typeCell.textContent = isDefault ? "Default" : "Custom";

        const actionsCell = document.createElement("td");
        actionsCell.className = "py-4 whitespace-nowrap space-x-3";

        if (!isDefault) {
            const editButton = document.createElement("button");
            editButton.type = "button";
            editButton.className = "text-blue-600 hover:text-blue-800";
            editButton.textContent = "Edit";
            editButton.addEventListener("click", () => {
                customIncomeCategoryName.value = category.name;
                incomeCategoryForm.dataset.editingId = category.id;

                saveIncomeCategoryBtn.innerHTML =
                    '<i class="fa-solid fa-floppy-disk mr-2"></i> Update Category';

                cancelIncomeCategoryEditBtn.classList.remove("hidden");
                showIncomeCategoryMessage("Editing: " + category.name);
                customIncomeCategoryName.focus();
            });

            const deleteButton = document.createElement("button");
            deleteButton.type = "button";
            deleteButton.className = "text-red-600 hover:text-red-800";
            deleteButton.textContent = "Delete";
            deleteButton.addEventListener("click", () => {
                deleteIncomeCategory(category);
            });

            actionsCell.append(editButton, deleteButton);
        } else {
            const protectedText = document.createElement("span");
            protectedText.className = "text-slate-400 text-sm";
            protectedText.textContent = "Protected";
            actionsCell.appendChild(protectedText);
        }

        row.append(nameCell, typeCell, actionsCell);
        incomeCategoryTableBody.appendChild(row);
    });
}

manageCategoriesBtn.addEventListener("click", async () => {
    categoryManager.classList.remove("hidden");
    showIncomeCategoryMessage("");

    try {
        await loadManagedIncomeCategories();
    } catch (error) {
        console.error("Income category loading error:", error);
        showIncomeCategoryMessage(error.message, "error");
    }
});

closeCategoryManagerBtn.addEventListener("click", () => {
    categoryManager.classList.add("hidden");
    resetIncomeCategoryForm();
    showIncomeCategoryMessage("");
});

cancelIncomeCategoryEditBtn.addEventListener("click", () => {
    resetIncomeCategoryForm();
    showIncomeCategoryMessage("Edit cancelled.");
});

incomeCategoryForm.addEventListener("submit", async event => {
    event.preventDefault();

    const name = customIncomeCategoryName.value.trim();
    const editingId = incomeCategoryForm.dataset.editingId;

    if (!name) {
        showIncomeCategoryMessage("Please enter a category name.", "error");
        return;
    }

    // Friendly client-side duplicate check; the backend also validates duplicates.
    const duplicate = managedIncomeCategories.some(category =>
        category.name.toLowerCase() === name.toLowerCase() &&
        String(category.id) !== String(editingId || "")
    );

    if (duplicate) {
        showIncomeCategoryMessage(
            "A category with this name already exists.",
            "error"
        );
        return;
    }

    saveIncomeCategoryBtn.disabled = true;

    try {
        const url = editingId
            ? `${BASE_URL}/income-categories/${editingId}`
            : `${BASE_URL}/income-categories`;

        await incomeCategoryApiRequest(url, {
            method: editingId ? "PUT" : "POST",
            body: JSON.stringify({ name })
        });

        resetIncomeCategoryForm();

        showIncomeCategoryMessage(
            editingId
                ? "Income category updated successfully."
                : "Income category added successfully."
        );

        // Refresh the income form, filter, category manager and history.
        await loadCategories();
        await loadManagedIncomeCategories();
        await loadIncomes();
    } catch (error) {
        console.error("Income category save error:", error);
        showIncomeCategoryMessage(error.message, "error");
    } finally {
        saveIncomeCategoryBtn.disabled = false;
    }
});

async function deleteIncomeCategory(category) {
    if (category.isDefault === true) {
        showIncomeCategoryMessage(
            "Default categories cannot be deleted.",
            "error"
        );
        return;
    }

    const confirmed = confirm(
        `Delete the category "${category.name}"? This cannot be undone.`
    );

    if (!confirmed) return;

    try {
        await incomeCategoryApiRequest(
            `${BASE_URL}/income-categories/${category.id}`,
            { method: "DELETE" }
        );

        resetIncomeCategoryForm();
        showIncomeCategoryMessage("Income category deleted successfully.");

        await loadCategories();
        await loadManagedIncomeCategories();
        await loadIncomes();
    } catch (error) {
        console.error("Income category deletion error:", error);
        showIncomeCategoryMessage(error.message, "error");
    }
}
