checkAuth();

console.log("Expenses JS loaded");


// ===============================
// ELEMENTS
// ===============================

const addExpenseBtn = document.getElementById("addExpenseBtn");
const expenseTableBody = document.getElementById("expenseTableBody");


// ===============================
// CREATE EXPENSE MODAL
// ===============================

const modal = document.createElement("div");

modal.id = "expenseModal";

modal.className =
    "fixed inset-0 bg-black/50 flex items-center justify-center z-50 hidden";

modal.innerHTML = `

    <div class="bg-white rounded-2xl shadow-xl w-full max-w-2xl mx-4 p-8">

        <!-- Modal Header -->

        <div class="flex justify-between items-center mb-6">

            <div>
                <h2 class="text-2xl font-bold text-slate-800">
                    Add New Expense
                </h2>

                <p class="text-slate-500 text-sm mt-1">
                    Enter your expense details
                </p>
            </div>

            <button
                id="closeExpenseModal"
                class="text-slate-500 hover:text-red-600 text-xl">

                <i class="fa-solid fa-xmark"></i>

            </button>

        </div>


        <!-- Expense Form -->

        <form id="expenseForm">

            <!-- Category + Target -->

            <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">

                <div>

                    <label class="block mb-2 font-semibold">
                        Category
                    </label>

                    <select
                        id="expenseCategory"
                        required
                        class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

                        <option value="">
                            Select Category
                        </option>

                    </select>

                </div>


                <div>

                    <label class="block mb-2 font-semibold">
                        Target
                    </label>

                    <select
                        id="expenseTarget"
                        required
                        class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

                        <option value="">
                            Select Target
                        </option>

                    </select>

                </div>

            </div>


            <!-- Title -->

            <div class="mb-5">

                <label class="block mb-2 font-semibold">
                    Title
                </label>

                <input
                    type="text"
                    id="expenseTitle"
                    maxlength="100"
                    required
                    placeholder="Example: Monthly Groceries"
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

            </div>


            <!-- Amount + Payment Method -->

            <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">

                <div>

                    <label class="block mb-2 font-semibold">
                        Amount
                    </label>

                    <input
                        type="number"
                        id="expenseAmount"
                        min="0.01"
                        step="0.01"
                        required
                        placeholder="₹ 0.00"
                        class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

                </div>


                <div>

                    <label class="block mb-2 font-semibold">
                        Payment Method
                    </label>

                    <select
                        id="paymentMethod"
                        required
                        class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

                        <option value="">
                            Select Payment Method
                        </option>

                        <option value="UPI">
                            UPI
                        </option>

                        <option value="Cash">
                            Cash
                        </option>

                        <option value="Credit Card">
                            Credit Card
                        </option>

                        <option value="Debit Card">
                            Debit Card
                        </option>

                        <option value="Net Banking">
                            Net Banking
                        </option>

                    </select>

                </div>

            </div>


            <!-- Date -->

            <div class="mb-5">

                <label class="block mb-2 font-semibold">
                    Expense Date
                </label>

                <input
                    type="date"
                    id="expenseDate"
                    required
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500">

            </div>


            <!-- Description -->

            <div class="mb-6">

                <label class="block mb-2 font-semibold">
                    Description
                </label>

                <textarea
                    id="expenseDescription"
                    maxlength="255"
                    rows="3"
                    placeholder="Add some details about this expense..."
                    class="w-full rounded-xl border border-gray-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>

            </div>


            <!-- Buttons -->

            <div class="flex justify-end gap-3">

                <button
                    type="button"
                    id="cancelExpenseBtn"
                    class="px-5 py-3 rounded-xl border border-gray-300 hover:bg-gray-100">

                    Cancel

                </button>


                <button
                    type="submit"
                    class="btn-primary px-6 py-3 rounded-xl font-semibold">

                    <i class="fa-solid fa-plus mr-2"></i>
                    Add Expense

                </button>

            </div>

        </form>

    </div>

`;

document.body.appendChild(modal);


// ===============================
// OPEN MODAL
// ===============================
addExpenseBtn.addEventListener("click", async function () {

    // Reset form for adding a new expense
    const form = document.getElementById("expenseForm");

    form.reset();
    delete form.dataset.editingId;

    // Change modal back to Add mode
    document.querySelector("#expenseModal h2").textContent =
        "Add New Expense";

    document.querySelector(
        "#expenseForm button[type='submit']"
    ).innerHTML = `
        <i class="fa-solid fa-plus mr-2"></i>
        Add Expense
    `;

    modal.classList.remove("hidden");

    await loadCategories();
    await loadTargets();

});


// ===============================
// CLOSE MODAL
// ===============================

document.getElementById("closeExpenseModal").addEventListener("click", closeModal);

document.getElementById("cancelExpenseBtn").addEventListener("click", closeModal);


function closeModal() {

    modal.classList.add("hidden");

}


// ===============================
// LOAD CATEGORIES
// ===============================

async function loadCategories() {

    const categorySelect =
        document.getElementById("expenseCategory");

    try {

        const response = await fetch(
            `${BASE_URL}/expense-categories`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${getToken()}`
                }
            }
        );


        if (!response.ok) {

            throw new Error("Failed to load categories");

        }


        const categories = await response.json();


        categorySelect.innerHTML = `
            <option value="">
                Select Category
            </option>
        `;


        categories.forEach(category => {

            const option = document.createElement("option");

            option.value = category.id;

            option.textContent = category.name;

            categorySelect.appendChild(option);

        });


    } catch (error) {

        console.error("Category loading error:", error);

        alert("Unable to load expense categories.");

    }

}


// ===============================
// LOAD TARGETS
// ===============================

async function loadTargets() {

    const targetSelect =
        document.getElementById("expenseTarget");

    try {

        const response = await fetch(
            `${BASE_URL}/expense-targets`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${getToken()}`
                }
            }
        );


        if (!response.ok) {

            throw new Error("Failed to load targets");

        }


        const targets = await response.json();


        targetSelect.innerHTML = `
            <option value="">
                Select Target
            </option>
        `;


        targets.forEach(target => {

            const option = document.createElement("option");

            option.value = target.id;

            option.textContent = target.name;

            targetSelect.appendChild(option);

        });


    } catch (error) {

        console.error("Target loading error:", error);

        alert("Unable to load expense targets.");

    }

}



// ===============================
// SUBMIT / UPDATE EXPENSE
// ===============================

document.getElementById("expenseForm").addEventListener(
    "submit",
    async function (e) {

        e.preventDefault();

        const form = document.getElementById("expenseForm");

        // Check whether we are editing
        const editingId = form.dataset.editingId;

        const categoryId =
            document.getElementById("expenseCategory").value;

        const targetId =
            document.getElementById("expenseTarget").value;

        const title =
            document.getElementById("expenseTitle").value.trim();

        const amount =
            document.getElementById("expenseAmount").value;

        const paymentMethod =
            document.getElementById("paymentMethod").value;

        const expenseDate =
            document.getElementById("expenseDate").value;

        const description =
            document.getElementById("expenseDescription").value.trim();


        // ===============================
        // REQUEST BODY
        // ===============================

        const expenseData = {

            expenseCategoryId: Number(categoryId),

            expenseTargetId: Number(targetId),

            amount: Number(amount),

            title: title,

            description: description,

            paymentMethod: paymentMethod,

            expenseDate: expenseDate

        };


        console.log(
            editingId
                ? "Updating expense:"
                : "Creating expense:",
            expenseData
        );


        try {

            // ===============================
            // URL + METHOD
            // ===============================

            const url = editingId
                ? `${BASE_URL}/expenses/${editingId}`
                : `${BASE_URL}/expenses`;

            const method = editingId
                ? "PUT"
                : "POST";


            const response = await fetch(
                url,
                {
                    method: method,

                    headers: {

                        "Content-Type": "application/json",

                        "Authorization":
                            `Bearer ${getToken()}`

                    },

                    body: JSON.stringify(expenseData)

                }
            );


            // ===============================
            // RESPONSE
            // ===============================

            let data = null;

            const responseText = await response.text();

            if (responseText) {
                try {
                    data = JSON.parse(responseText);
                } catch {
                    data = responseText;
                }
            }


            if (response.ok) {

                if (editingId) {

                    alert("Expense updated successfully! 🎉");

                } else {

                    alert("Expense added successfully! 🎉");

                }


                console.log(
                    editingId
                        ? "Updated expense:"
                        : "Created expense:",
                    data
                );


                // Reset form
                form.reset();

                // Remove editing mode
                delete form.dataset.editingId;

                // Reset modal title
                document.querySelector("#expenseModal h2").textContent =
                    "Add New Expense";


                // Reset button
                document.querySelector(
                    "#expenseForm button[type='submit']"
                ).innerHTML = `
                    <i class="fa-solid fa-plus mr-2"></i>
                    Add Expense
                `;


                // Close modal
                closeModal();


                // Refresh expense history
                await loadExpenses();


            } else {

                console.error(
                    editingId
                        ? "Expense update failed:"
                        : "Expense creation failed:",
                    data
                );


                alert(
                    data?.message ||
                    (
                        editingId
                            ? "Unable to update expense."
                            : "Unable to add expense."
                    )
                );

            }


        } catch (error) {

            console.error("Server error:", error);

            alert("Unable to connect to server.");

        }

    }
);
// ===============================
// LOAD EXPENSES
// ===============================

async function loadExpenses() {

    try {

        const response = await fetch(
            `${BASE_URL}/expenses`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${getToken()}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load expenses");
        }

        const expenses = await response.json();

        console.log("Expenses received:", expenses);

        expenseTableBody.innerHTML = "";

        if (expenses.length === 0) {

            expenseTableBody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center py-10 text-slate-500">
                        No expenses found.
                    </td>
                </tr>
            `;

            return;
        }

        expenses.forEach(expense => {

            const row = document.createElement("tr");

            row.className = "border-b";

            row.innerHTML = `

                <td class="py-4 font-medium">
                    ${expense.title}
                </td>

                <td>
                    ${expense.expenseCategory}
                </td>

                <td class="text-red-600 font-semibold">
                    ₹${Number(expense.amount).toLocaleString("en-IN", {
                        minimumFractionDigits: 2
                    })}
                </td>

                <td>
                    ${formatDate(expense.expenseDate)}
                </td>

                <td>

                    <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm">
                        Paid
                    </span>

                </td>

                <td class="space-x-3">

                    <button
                        class="text-blue-600"
                        onclick="editExpense(${expense.id})">

                        <i class="fa-solid fa-pen"></i>

                    </button>

                    <button
                        class="text-red-600"
                        onclick="deleteExpense(${expense.id})">

                        <i class="fa-solid fa-trash"></i>

                    </button>

                </td>
            `;

            expenseTableBody.appendChild(row);

        });

    } catch (error) {

        console.error("Error loading expenses:", error);

        expenseTableBody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center py-10 text-red-500">
                    Unable to load expenses.
                </td>
            </tr>
        `;
    }
}
// ===============================
// FORMAT DATE
// ===============================

function formatDate(dateString) {

    const date = new Date(dateString);

    return date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}
loadExpenses();
// ===============================
// DELETE EXPENSE
// ===============================

async function deleteExpense(id) {

    const confirmDelete = confirm(
        "Are you sure you want to delete this expense?"
    );

    if (!confirmDelete) {
        return;
    }

    try {

        const response = await fetch(
            `${BASE_URL}/expenses/${id}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization": `Bearer ${getToken()}`
                }
            }
        );

        if (response.ok) {

            alert("Expense deleted successfully.");

            // Refresh expense history
            await loadExpenses();

        } else {

            const data = await response.text();

            console.error("Delete failed:", data);

            alert("Unable to delete expense.");

        }

    } catch (error) {

        console.error("Delete error:", error);

        alert("Unable to connect to server.");

    }
}
// ===============================
// EDIT EXPENSE
// ===============================

async function editExpense(id) {

    try {

        // Get expense details
        const response = await fetch(
            `${BASE_URL}/expenses/${id}`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${getToken()}`
                }
            }
        );


        if (!response.ok) {

            throw new Error("Failed to fetch expense");

        }


        const expense = await response.json();

        console.log("Expense to edit:", expense);


        // ===============================
        // OPEN MODAL
        // ===============================

        modal.classList.remove("hidden");


        // ===============================
        // LOAD CATEGORIES AND TARGETS
        // ===============================

        await loadCategories();

        await loadTargets();


        // ===============================
        // FILL EXISTING EXPENSE DATA
        // ===============================

        document.getElementById("expenseTitle").value =
            expense.title || "";


        document.getElementById("expenseAmount").value =
            expense.amount || "";


        document.getElementById("paymentMethod").value =
            expense.paymentMethod || "";


        document.getElementById("expenseDate").value =
            expense.expenseDate || "";


        document.getElementById("expenseDescription").value =
            expense.description || "";


        // ===============================
        // SELECT CATEGORY
        // ===============================

        const categorySelect =
            document.getElementById("expenseCategory");

        const categoryOption =
            [...categorySelect.options].find(
                option =>
                    option.textContent.trim() ===
                    expense.expenseCategory
            );


        if (categoryOption) {

            categorySelect.value =
                categoryOption.value;

        }


        // ===============================
        // SELECT TARGET
        // ===============================

        const targetSelect =
            document.getElementById("expenseTarget");

        const targetOption =
            [...targetSelect.options].find(
                option =>
                    option.textContent.trim() ===
                    expense.expenseTarget
            );


        if (targetOption) {

            targetSelect.value =
                targetOption.value;

        }


        // ===============================
        // CHANGE MODAL TO EDIT MODE
        // ===============================

        document.querySelector("#expenseModal h2").textContent =
            "Edit Expense";


        document.querySelector(
            "#expenseForm button[type='submit']"
        ).innerHTML = `
            <i class="fa-solid fa-check mr-2"></i>
            Update Expense
        `;


        // ===============================
        // STORE EXPENSE ID
        // ===============================

        document.getElementById("expenseForm")
            .dataset.editingId = id;


    } catch (error) {

        console.error(
            "Error loading expense:",
            error
        );

        alert(
            "Unable to load expense details."
        );

    }

}