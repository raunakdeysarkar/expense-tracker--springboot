let allExpenses = [];


// ================= LOAD DATA =================

async function loadExpenses() {

    const response = await fetch("/expenses");

    if (!response.ok) {
        console.error("Could not load expenses");
        return;
    }

    allExpenses = await response.json();

    updateDashboard();

    renderRecentExpenses();

    renderAllExpenses();

    renderSummary();

    renderCategories();

    renderMonths();
}


// ================= DASHBOARD =================

function updateDashboard() {

    let total = 0;

    const categories = new Set();

    for (const expense of allExpenses) {

        total += expense.amount;

        if (expense.category) {
            categories.add(expense.category);
        }
    }


    document.getElementById("totalExpenses").textContent =
        `₹${total}`;


    document.getElementById("transactionCount").textContent =
        allExpenses.length;


    document.getElementById("categoryCount").textContent =
        categories.size;


    const now = new Date();

    const currentMonth =
        now.getFullYear() +
        "-" +
        String(now.getMonth() + 1).padStart(2, "0");


    let monthlyTotal = 0;

    for (const expense of allExpenses) {

        if (expense.date.startsWith(currentMonth)) {

            monthlyTotal += expense.amount;

        }
    }


    document.getElementById("monthlyExpenses").textContent =
        `₹${monthlyTotal}`;


    document.getElementById("currentMonth").textContent =
        currentMonth;
}


// ================= RECENT EXPENSES =================

function renderRecentExpenses() {

    const container =
        document.getElementById("recentExpenseList");

    container.innerHTML = "";


    const recentExpenses =
        [...allExpenses]
            .sort((a, b) => b.id - a.id)
            .slice(0, 5);


    recentExpenses.forEach(expense => {

        container.innerHTML += createExpenseRow(expense);

    });
}


// ================= ALL EXPENSES =================

function renderAllExpenses() {

    const container =
        document.getElementById("allExpenseList");

    container.innerHTML = "";


    const expenses =
        [...allExpenses]
            .sort((a, b) => b.id - a.id);


    expenses.forEach(expense => {

        container.innerHTML += createExpenseRow(
            expense,
            true
        );

    });
}


// ================= EXPENSE ROW =================

function createExpenseRow(expense, showDelete = false) {

    return `
        <div class="expense-row">

            <span class="expense-date">
                ${expense.date}
            </span>

            <span>
                ${expense.description}
            </span>

            <span class="expense-category">
                ${expense.category}
            </span>

            <span class="expense-amount">
                ₹${expense.amount}

                ${
                    showDelete
                    ?
                    `<button
                        class="delete-button"
                        onclick="deleteExpense(${expense.id})">
                        Delete
                    </button>`
                    :
                    ""
                }

            </span>

        </div>
    `;
}


// ================= SUMMARY =================

function renderSummary() {

    let total = 0;


    for (const expense of allExpenses) {

        total += expense.amount;

    }


    const average =
        allExpenses.length === 0
        ? 0
        : total / allExpenses.length;


    document.getElementById("summaryTotal").textContent =
        `₹${total}`;


    document.getElementById("summaryTransactions").textContent =
        allExpenses.length;


    document.getElementById("averageExpense").textContent =
        `₹${average.toFixed(2)}`;
}


// ================= CATEGORY =================

function renderCategories() {

    const container =
        document.getElementById("categoryList");

    container.innerHTML = "";


    const categoryTotals = {};


    for (const expense of allExpenses) {

        const category =
            expense.category || "Other";


        if (!categoryTotals[category]) {

            categoryTotals[category] = 0;

        }


        categoryTotals[category] += expense.amount;
    }


    for (const category in categoryTotals) {

        container.innerHTML += `

            <div class="category-card">

                <div>

                    <h3>
                        ${category}
                    </h3>

                    <span>
                        Category
                    </span>

                </div>

                <strong>
                    ₹${categoryTotals[category]}
                </strong>

            </div>

        `;
    }
}


// ================= MONTH =================

function renderMonths() {

    const container =
        document.getElementById("monthList");

    container.innerHTML = "";


    const monthTotals = {};


    for (const expense of allExpenses) {

        const month =
            expense.date.substring(0, 7);


        if (!monthTotals[month]) {

            monthTotals[month] = 0;

        }


        monthTotals[month] += expense.amount;
    }


    const months =
        Object.keys(monthTotals).sort().reverse();


    months.forEach(month => {

        container.innerHTML += `

            <div class="category-card">

                <div>

                    <h3>
                        ${month}
                    </h3>

                    <span>
                        Monthly spending
                    </span>

                </div>

                <strong>
                    ₹${monthTotals[month]}
                </strong>

            </div>

        `;

    });
}


// ================= ADD EXPENSE =================

async function addExpense() {

    const expense = {

        date:
            document.getElementById("date").value,

        description:
            document.getElementById("description").value,

        amount:
            Number(
                document.getElementById("amount").value
            ),

        category:
            document.getElementById("category").value

    };


    const response =
        await fetch("/expenses", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(expense)

        });


    if (!response.ok) {

        alert("Invalid expense data.");

        return;
    }


    document.getElementById("date").value = "";

    document.getElementById("description").value = "";

    document.getElementById("amount").value = "";

    document.getElementById("category").value = "";


    document.getElementById("addExpenseSection")
        .style.display = "none";


    await loadExpenses();

    showPage("dashboard");
}


// ================= DELETE =================

async function deleteExpense(id) {

    const confirmed =
        confirm("Delete this expense?");


    if (!confirmed) {
        return;
    }


    const response =
        await fetch(`/expenses/${id}`, {

            method: "DELETE"

        });


    if (response.ok) {

        await loadExpenses();

    } else {

        alert("Could not delete expense.");

    }
}


// ================= PAGE NAVIGATION =================

function showPage(page) {

    const pages = [
        "dashboard",
        "expenses",
        "summary",
        "category",
        "month"
    ];


    pages.forEach(name => {

        const element =
            document.getElementById(
                name + "Page"
            );


        if (name === page) {

            element.classList.remove("hidden");

        } else {

            element.classList.add("hidden");

        }

    });


    const buttons =
        document.querySelectorAll(".nav-button");


    buttons.forEach(button => {

        button.classList.remove("active");

    });


    const pageIndex = pages.indexOf(page);


    if (pageIndex !== -1) {

        buttons[pageIndex]
            .classList.add("active");

    }


    document.getElementById("addExpenseSection")
        .style.display = "none";
}


// ================= ADD EXPENSE FORM =================

function showAddExpense() {

    const section =
        document.getElementById(
            "addExpenseSection"
        );


    if (section.style.display === "block") {

        section.style.display = "none";

    } else {

        section.style.display = "block";

        section.scrollIntoView({
            behavior: "smooth"
        });

    }
}


// ================= START APPLICATION =================

loadExpenses();
function exportExpenses() {
    window.location.href = "/expenses/export";
}
