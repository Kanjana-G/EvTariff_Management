/* =========================================================
   EV TARIFF MANAGEMENT SYSTEM
   app.js
========================================================= */


/* =========================================================
   INITIAL LOAD
========================================================= */

/* =========================================================
   AUTHENTICATION
========================================================= */

let currentUser = null;
let currentUserRole = null;


/* =========================================================
   INITIAL LOAD
========================================================= */

document.addEventListener("DOMContentLoaded", async function () {

    const authenticated = await checkAuthentication();

    if (!authenticated) {
        return;
    }

    applyRoleVisibility();

    loadDashboard();
});


/* =========================================================
   CHECK LOGIN
========================================================= */

async function checkAuthentication() {

    try {

        const response = await fetch("/api/auth/me");

        if (!response.ok) {

            window.location.href = "/login.html";

            return false;
        }

        const user = await response.json();

        currentUser = user;
        currentUserRole = user.role.toUpperCase();


        const usernameElement =
            document.getElementById("logged-username");

        const roleElement =
            document.getElementById("logged-role");


        if (usernameElement) {
            usernameElement.innerText = user.username;
        }

        if (roleElement) {
            roleElement.innerText = user.role;
        }


        return true;

    } catch (error) {

        console.error(
            "Authentication check failed:",
            error
        );

        window.location.href = "/login.html";

        return false;
    }
}


/* =========================================================
   LOGOUT
========================================================= */

async function logout() {

    try {

        await fetch("/api/auth/logout", {
            method: "POST"
        });

    } catch (error) {

        console.error(
            "Logout error:",
            error
        );
    }

    window.location.href = "/login.html";
}


/* =========================================================
   ROLE BASED FRONTEND
========================================================= */

function applyRoleVisibility() {

    if (!currentUserRole) {
        return;
    }


    const navDashboard =
        document.getElementById("nav-dashboard");

    const navStations =
        document.getElementById("nav-stations");

    const navChargers =
        document.getElementById("nav-chargers");

    const navTariffs =
        document.getElementById("nav-tariffs");

    const navSessions =
        document.getElementById("nav-sessions");

    const navInvoices =
        document.getElementById("nav-invoices");

    const navPayments =
        document.getElementById("nav-payments");

    const navExpenses =
        document.getElementById("nav-expenses");

    const navBudgets =
        document.getElementById("nav-budgets");

    const navReports =
        document.getElementById("nav-reports");


    /*
       First hide everything except Dashboard.
    */

    [
        navStations,
        navChargers,
        navTariffs,
        navSessions,
        navInvoices,
        navPayments,
        navExpenses,
        navBudgets,
        navReports

    ].forEach(button => {

        if (button) {
            button.style.display = "none";
        }

    });


    /*
       ADMIN
    */

    if (currentUserRole === "ADMIN") {

        [
            navStations,
            navChargers,
            navTariffs,
            navSessions,
            navInvoices,
            navPayments,
            navExpenses,
            navBudgets,
            navReports

        ].forEach(button => {

            if (button) {
                button.style.display = "";
            }

        });
    }


    /*
       ACCOUNTANT
    */

    else if (currentUserRole === "ACCOUNTANT") {

        [
            navInvoices,
            navPayments,
            navExpenses,
            navBudgets,
            navReports

        ].forEach(button => {

            if (button) {
                button.style.display = "";
            }

        });
    }


    /*
       DRIVER
    */

    else if (currentUserRole === "DRIVER") {

        [
            navStations,
            navChargers,
            navTariffs,
            navSessions

        ].forEach(button => {

            if (button) {
                button.style.display = "";
            }

        });
    }


    applyStaticButtonPermissions();
}


/* =========================================================
   STATIC BUTTON PERMISSIONS
========================================================= */

function applyStaticButtonPermissions() {

    /*
       These are the Add/Generate/Payment buttons
       already present in index.html.
    */

    const stationAdd =
        document.querySelector(
            `button[onclick="showStationForm()"]`
        );

    const chargerAdd =
        document.querySelector(
            `button[onclick="showChargerForm()"]`
        );

    const tariffAdd =
        document.querySelector(
            `button[onclick="showTariffForm()"]`
        );

    const sessionAdd =
        document.querySelector(
            `button[onclick="showSessionForm()"]`
        );

    const invoiceAdd =
        document.querySelector(
            `button[onclick="showInvoiceForm()"]`
        );

    const paymentAdd =
        document.querySelector(
            `button[onclick="showPaymentForm()"]`
        );

    const expenseAdd =
        document.querySelector(
            `button[onclick="showExpenseForm()"]`
        );

    const budgetAdd =
        document.querySelector(
            `button[onclick="showBudgetForm()"]`
        );


    /*
       Hide all first
    */

    [
        stationAdd,
        chargerAdd,
        tariffAdd,
        sessionAdd,
        invoiceAdd,
        paymentAdd,
        expenseAdd,
        budgetAdd

    ].forEach(button => {

        if (button) {
            button.style.display = "none";
        }

    });


    /*
       ADMIN
    */

    if (currentUserRole === "ADMIN") {

        if (stationAdd) {
            stationAdd.style.display = "";
        }

        if (chargerAdd) {
            chargerAdd.style.display = "";
        }

        if (tariffAdd) {
            tariffAdd.style.display = "";
        }

        if (budgetAdd) {
            budgetAdd.style.display = "";
        }
    }


    /*
       ACCOUNTANT
    */

    else if (currentUserRole === "ACCOUNTANT") {

        if (invoiceAdd) {
            invoiceAdd.style.display = "";
        }

        if (paymentAdd) {
            paymentAdd.style.display = "";
        }

        if (expenseAdd) {
            expenseAdd.style.display = "";
        }
    }


    /*
       DRIVER
    */

    else if (currentUserRole === "DRIVER") {

        if (sessionAdd) {
            sessionAdd.style.display = "";
        }
    }
}


/* =========================================================
   PAGE NAVIGATION
========================================================= */

function showPage(pageId) {

    const pages = document.querySelectorAll(".page");

    pages.forEach(page => {
        page.classList.remove("active-page");
    });


    const buttons = document.querySelectorAll(".nav-btn");

    buttons.forEach(button => {
        button.classList.remove("active");
    });


    const selectedPage = document.getElementById(pageId);

    if (selectedPage) {
        selectedPage.classList.add("active-page");
    }


    // Highlight clicked sidebar button
    if (
        typeof event !== "undefined" &&
        event.currentTarget
    ) {
        event.currentTarget.classList.add("active");
    }


    const titles = {
        dashboard: "Dashboard",
        stations: "Stations",
        chargers: "Chargers",
        tariffs: "Tariffs",
        sessions: "Charging Sessions",
        invoices: "Invoices",
        payments: "Payments",
        expenses: "Utility Expenses",
        budgets: "Budgets",
        reports: "Reports"
    };


    document.getElementById("page-title").innerText =
        titles[pageId] || "EV Tariff Management";


    /* Load corresponding page data */

    if (pageId === "dashboard") {
        loadDashboard();
    }

    if (pageId === "stations") {
        loadStations();
    }

    if (pageId === "chargers") {
        loadChargers();
    }

    if (pageId === "tariffs") {
        loadTariffs();
    }

    if (pageId === "sessions") {
        loadSessions();
    }

    if (pageId === "invoices") {
        loadInvoices();
    }

    if (pageId === "payments") {
        loadPayments();
    }

    if (pageId === "expenses") {
    loadExpenses();
    }

    if (pageId === "budgets") {
    loadBudgets();
    }

    if (pageId === "reports") {
    loadReports();
    }
}


/* =========================================================
   COMMON FUNCTIONS
========================================================= */

function formatTime(time) {

    if (!time) {
        return "-";
    }

    return time.substring(0, 5);
}


function formatDateTime(dateTime) {

    if (!dateTime) {
        return "-";
    }

    const date = new Date(dateTime);

    return date.toLocaleString();
}


/* =========================================================
   DASHBOARD
========================================================= */

async function loadDashboard() {

    resetDashboard();


    /*
       ADMIN
    */

    if (currentUserRole === "ADMIN") {

        await loadStationCount();

        await loadAvailableChargers();

        await loadActiveSessions();

        await loadRevenue();

        return;
    }


    /*
       ACCOUNTANT
    */

    if (currentUserRole === "ACCOUNTANT") {

        await loadStationCount();

        await loadRevenue();

        return;
    }


    /*
       DRIVER
    */

    if (currentUserRole === "DRIVER") {

        await loadStationCount();

        await loadAvailableChargers();

        return;
    }
}


/* =========================================================
   RESET DASHBOARD
========================================================= */

function resetDashboard() {

    const stationCount =
        document.getElementById("station-count");

    const chargerCount =
        document.getElementById("charger-count");

    const sessionCount =
        document.getElementById("session-count");

    const revenue =
        document.getElementById("revenue");


    if (stationCount) {
        stationCount.innerText = "-";
    }

    if (chargerCount) {
        chargerCount.innerText = "-";
    }

    if (sessionCount) {
        sessionCount.innerText = "-";
    }

    if (revenue) {
        revenue.innerText = "-";
    }
}


/* -------------------------
   STATION COUNT
------------------------- */

async function loadStationCount() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();


        document.getElementById(
            "station-count"
        ).innerText =
            stations.length;


    } catch (error) {

        console.error(
            "Error loading station count:",
            error
        );
    }
}


/* -------------------------
   AVAILABLE CHARGERS
------------------------- */

async function loadAvailableChargers() {

    try {

        const response =
            await fetch("/api/ev/chargers");

        const chargers =
            await response.json();


        const available =
            chargers.filter(charger =>

                charger.status &&

                charger.status.toUpperCase()
                    === "AVAILABLE"
            );


        document.getElementById(
            "charger-count"
        ).innerText =
            available.length;


    } catch (error) {

        console.error(
            "Error loading available chargers:",
            error
        );
    }
}


/* -------------------------
   ACTIVE SESSIONS
------------------------- */

async function loadActiveSessions() {

    try {

        const response =
            await fetch("/api/ev/sessions");

        const sessions =
            await response.json();


        const activeSessions =
            sessions.filter(session =>

                session.status &&

                session.status.toUpperCase()
                    === "ACTIVE"
            );


        document.getElementById(
            "session-count"
        ).innerText =
            activeSessions.length;


    } catch (error) {

        console.error(
            "Error loading active sessions:",
            error
        );
    }
}


/* -------------------------
   TOTAL REVENUE
------------------------- */

async function loadRevenue() {

    try {

        const response =
            await fetch("/api/ev/payments");

        const payments =
            await response.json();


        let totalRevenue = 0;


        payments.forEach(payment => {

            if (
                payment.paymentStatus &&
                payment.paymentStatus.toUpperCase()
                    === "SUCCESS"
            ) {

                totalRevenue +=
                    Number(payment.amount || 0);
            }
        });


        document.getElementById(
            "revenue"
        ).innerText =
            "₹" + totalRevenue.toFixed(2);


    } catch (error) {

        console.error(
            "Error loading revenue:",
            error
        );
    }
}


/* =========================================================
   STATIONS
========================================================= */

async function loadStations() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();


        const tableBody =
            document.getElementById(
                "station-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        stations.forEach(station => {

            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${station.id}</td>

                <td>${station.stationName}</td>

                <td>${station.location}</td>

                <td class="${
                    station.status === "ACTIVE"
                        ? "status-active"
                        : "status-inactive"
                }">
                    ${station.status}
                </td>

                <td>

    ${
        currentUserRole === "ADMIN"

        ? `
            <button
                class="edit-btn"
                onclick="editStation(${station.id})">
                Edit
            </button>

            <button
                class="delete-btn"
                onclick="deleteStation(${station.id})">
                Delete
            </button>
          `

        : "-"
    }

</td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading stations:",
            error
        );
    }
}


/* -------------------------
   SHOW STATION FORM
------------------------- */

function showStationForm() {

    document.getElementById(
        "station-form-title"
    ).innerText =
        "Add Station";


    document.getElementById(
        "station-id"
    ).value = "";


    document.getElementById(
        "station-name"
    ).value = "";


    document.getElementById(
        "station-location"
    ).value = "";


    document.getElementById(
        "station-status"
    ).value =
        "ACTIVE";


    document.getElementById(
        "station-form-box"
    ).classList.remove("hidden");
}


/* -------------------------
   HIDE STATION FORM
------------------------- */

function hideStationForm() {

    document.getElementById(
        "station-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   SAVE STATION
------------------------- */

async function saveStation() {

    const id =
        document.getElementById(
            "station-id"
        ).value;


    const stationName =
        document.getElementById(
            "station-name"
        ).value.trim();


    const location =
        document.getElementById(
            "station-location"
        ).value.trim();


    const status =
        document.getElementById(
            "station-status"
        ).value;


    if (
        stationName === "" ||
        location === ""
    ) {

        alert(
            "Please enter station name and location."
        );

        return;
    }


    const station = {

        stationName: stationName,

        location: location,

        status: status
    };


    let url =
        "/api/ev/stations";

    let method =
        "POST";


    if (id !== "") {

        url =
            "/api/ev/stations/" + id;

        method =
            "PUT";
    }


    try {

        const response =
            await fetch(url, {

                method: method,

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(station)
            });


        if (!response.ok) {

            alert(
                "Unable to save station."
            );

            return;
        }


        hideStationForm();

        await loadStations();

        await loadDashboard();


        if (method === "POST") {

            alert(
                "Station added successfully."
            );

        } else {

            alert(
                "Station updated successfully."
            );
        }


    } catch (error) {

        console.error(
            "Error saving station:",
            error
        );
    }
}


/* -------------------------
   EDIT STATION
------------------------- */

async function editStation(id) {

    try {

        const response =
            await fetch(
                "/api/ev/stations/" + id
            );


        const station =
            await response.json();


        document.getElementById(
            "station-id"
        ).value =
            station.id;


        document.getElementById(
            "station-name"
        ).value =
            station.stationName;


        document.getElementById(
            "station-location"
        ).value =
            station.location;


        document.getElementById(
            "station-status"
        ).value =
            station.status;


        document.getElementById(
            "station-form-title"
        ).innerText =
            "Edit Station";


        document.getElementById(
            "station-form-box"
        ).classList.remove("hidden");


    } catch (error) {

        console.error(
            "Error loading station:",
            error
        );
    }
}


/* -------------------------
   DELETE STATION
------------------------- */

async function deleteStation(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this station?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/stations/" + id,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to delete station."
            );

            return;
        }


        alert(
            "Station deleted successfully."
        );


        await loadStations();

        await loadDashboard();


    } catch (error) {

        console.error(
            "Error deleting station:",
            error
        );
    }
}


/* =========================================================
   CHARGERS
========================================================= */

async function loadChargers() {

    try {

        const response =
            await fetch("/api/ev/chargers");

        const chargers =
            await response.json();


        const tableBody =
            document.getElementById(
                "charger-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        chargers.forEach(charger => {

            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${charger.id}</td>

                <td>${charger.chargerName}</td>

                <td>${charger.chargerType}</td>

                <td>
                    ${
                        charger.station
                            ? charger.station.stationName
                            : "Not Assigned"
                    }
                </td>

                <td>

                    <span class="status-badge
                        ${getChargerStatusClass(charger.status)}">

                        ${charger.status}

                    </span>

                </td>

                <td>

    ${
        currentUserRole === "ADMIN"

        ? `
            <button
                class="edit-btn"
                onclick="editCharger(${charger.id})">
                Edit
            </button>

            <button
                class="delete-btn"
                onclick="deleteCharger(${charger.id})">
                Delete
            </button>
          `

        : "-"
    }

</td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading chargers:",
            error
        );
    }
}


/* -------------------------
   CHARGER STATUS CSS CLASS
------------------------- */

function getChargerStatusClass(status) {

    if (!status) {
        return "";
    }


    const value =
        status.toUpperCase();


    if (value === "AVAILABLE") {
        return "available";
    }


    if (value === "OCCUPIED") {
        return "occupied";
    }


    if (value === "MAINTENANCE") {
        return "maintenance";
    }


    return "";
}


/* -------------------------
   LOAD STATIONS FOR CHARGER
------------------------- */

async function loadStationDropdown() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();


        const select =
            document.getElementById(
                "charger-station"
            );


        select.innerHTML =
            `<option value="">Select Station</option>`;


        stations.forEach(station => {

            const option =
                document.createElement("option");


            option.value =
                station.id;


            option.textContent =
                station.stationName
                + " - "
                + station.location;


            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading station dropdown:",
            error
        );
    }
}


/* -------------------------
   SHOW CHARGER FORM
------------------------- */

async function showChargerForm() {

    await loadStationDropdown();


    document.getElementById(
        "charger-form-title"
    ).innerText =
        "Add Charger";


    document.getElementById(
        "charger-id"
    ).value = "";


    document.getElementById(
        "charger-name"
    ).value = "";


    document.getElementById(
        "charger-type"
    ).value =
        "AC";


    document.getElementById(
        "charger-status"
    ).value =
        "AVAILABLE";


    document.getElementById(
        "charger-station"
    ).value = "";


    document.getElementById(
        "charger-form-box"
    ).classList.remove("hidden");
}


/* -------------------------
   HIDE CHARGER FORM
------------------------- */

function hideChargerForm() {

    document.getElementById(
        "charger-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   SAVE CHARGER
------------------------- */

async function saveCharger() {

    const id =
        document.getElementById(
            "charger-id"
        ).value;


    const chargerName =
        document.getElementById(
            "charger-name"
        ).value.trim();


    const chargerType =
        document.getElementById(
            "charger-type"
        ).value;


    const status =
        document.getElementById(
            "charger-status"
        ).value;


    const stationId =
        document.getElementById(
            "charger-station"
        ).value;


    if (
        chargerName === "" ||
        stationId === ""
    ) {

        alert(
            "Please enter charger name and select a station."
        );

        return;
    }


    const charger = {

        chargerName: chargerName,

        chargerType: chargerType,

        status: status,

        station: {
            id: Number(stationId)
        }
    };


    let url =
        "/api/ev/chargers";

    let method =
        "POST";


    if (id !== "") {

        url =
            "/api/ev/chargers/" + id;

        method =
            "PUT";
    }


    try {

        const response =
            await fetch(url, {

                method: method,

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(charger)
            });


        if (!response.ok) {

            alert(
                "Unable to save charger."
            );

            return;
        }


        hideChargerForm();

        await loadChargers();

        await loadDashboard();


        if (method === "POST") {

            alert(
                "Charger added successfully."
            );

        } else {

            alert(
                "Charger updated successfully."
            );
        }


    } catch (error) {

        console.error(
            "Error saving charger:",
            error
        );
    }
}


/* -------------------------
   EDIT CHARGER
------------------------- */

async function editCharger(id) {

    try {

        await loadStationDropdown();


        const response =
            await fetch(
                "/api/ev/chargers/" + id
            );


        const charger =
            await response.json();


        document.getElementById(
            "charger-id"
        ).value =
            charger.id;


        document.getElementById(
            "charger-name"
        ).value =
            charger.chargerName;


        document.getElementById(
            "charger-type"
        ).value =
            charger.chargerType;


        document.getElementById(
            "charger-status"
        ).value =
            charger.status;


        if (charger.station) {

            document.getElementById(
                "charger-station"
            ).value =
                charger.station.id;
        }


        document.getElementById(
            "charger-form-title"
        ).innerText =
            "Edit Charger";


        document.getElementById(
            "charger-form-box"
        ).classList.remove("hidden");


    } catch (error) {

        console.error(
            "Error loading charger:",
            error
        );
    }
}


/* -------------------------
   DELETE CHARGER
------------------------- */

async function deleteCharger(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this charger?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/chargers/" + id,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to delete charger."
            );

            return;
        }


        alert(
            "Charger deleted successfully."
        );


        await loadChargers();

        await loadDashboard();


    } catch (error) {

        console.error(
            "Error deleting charger:",
            error
        );
    }
}


/* =========================================================
   TARIFFS
========================================================= */

async function loadTariffs() {

    try {

        const response =
            await fetch("/api/ev/tariffs");

        const tariffs =
            await response.json();


        const tableBody =
            document.getElementById(
                "tariff-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        tariffs.forEach(tariff => {

            const peakRate =
                Number(tariff.baseRate || 0) *
                Number(tariff.peakMultiplier || 0);


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${tariff.id}</td>

                <td>
                    ${
                        tariff.station
                            ? tariff.station.stationName
                            : "Not Assigned"
                    }
                </td>

                <td>
                    ₹${Number(
                        tariff.baseRate || 0
                    ).toFixed(2)} / kWh
                </td>

                <td>
                    ${formatTime(tariff.peakStartTime)}
                    -
                    ${formatTime(tariff.peakEndTime)}
                </td>

                <td>
                    ${tariff.peakMultiplier}x
                </td>

                <td>
                    <strong>
                        ₹${peakRate.toFixed(2)} / kWh
                    </strong>
                </td>

                <td>

    ${
        currentUserRole === "ADMIN"

        ? `
            <button
                class="edit-btn"
                onclick="editTariff(${tariff.id})">
                Edit
            </button>

            <button
                class="delete-btn"
                onclick="deleteTariff(${tariff.id})">
                Delete
            </button>
          `

        : "-"
    }

</td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading tariffs:",
            error
        );
    }
}


/* -------------------------
   LOAD STATIONS FOR TARIFF
------------------------- */

async function loadTariffStationDropdown() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();


        const select =
            document.getElementById(
                "tariff-station"
            );


        select.innerHTML =
            `<option value="">Select Station</option>`;


        stations.forEach(station => {

            const option =
                document.createElement("option");


            option.value =
                station.id;


            option.textContent =
                station.stationName
                + " - "
                + station.location;


            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading tariff stations:",
            error
        );
    }
}


/* -------------------------
   SHOW TARIFF FORM
------------------------- */

async function showTariffForm() {

    await loadTariffStationDropdown();


    document.getElementById(
        "tariff-form-title"
    ).innerText =
        "Add Tariff";


    document.getElementById(
        "tariff-id"
    ).value = "";


    document.getElementById(
        "tariff-station"
    ).value = "";


    document.getElementById(
        "tariff-base-rate"
    ).value = "";


    document.getElementById(
        "tariff-multiplier"
    ).value =
        "1.5";


    document.getElementById(
        "tariff-start"
    ).value =
        "17:00";


    document.getElementById(
        "tariff-end"
    ).value =
        "21:00";


    document.getElementById(
        "tariff-form-box"
    ).classList.remove("hidden");
}


/* -------------------------
   HIDE TARIFF FORM
------------------------- */

function hideTariffForm() {

    document.getElementById(
        "tariff-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   SAVE TARIFF
------------------------- */

async function saveTariff() {

    const id =
        document.getElementById(
            "tariff-id"
        ).value;


    const stationId =
        document.getElementById(
            "tariff-station"
        ).value;


    const baseRate =
        document.getElementById(
            "tariff-base-rate"
        ).value;


    const multiplier =
        document.getElementById(
            "tariff-multiplier"
        ).value;


    const startTime =
        document.getElementById(
            "tariff-start"
        ).value;


    const endTime =
        document.getElementById(
            "tariff-end"
        ).value;


    if (
        stationId === "" ||
        baseRate === "" ||
        multiplier === "" ||
        startTime === "" ||
        endTime === ""
    ) {

        alert(
            "Please fill all tariff details."
        );

        return;
    }


    if (
        Number(baseRate) <= 0 ||
        Number(multiplier) < 1
    ) {

        alert(
            "Please enter a valid rate and multiplier."
        );

        return;
    }


    const tariff = {

        baseRate:
            Number(baseRate),

        peakStartTime:
            startTime,

        peakEndTime:
            endTime,

        peakMultiplier:
            Number(multiplier),

        station: {
            id: Number(stationId)
        }
    };


    let url =
        "/api/ev/tariffs";

    let method =
        "POST";


    if (id !== "") {

        url =
            "/api/ev/tariffs/" + id;

        method =
            "PUT";
    }


    try {

        const response =
            await fetch(url, {

                method: method,

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(tariff)
            });


        if (!response.ok) {

            alert(
                "Unable to save tariff."
            );

            return;
        }


        hideTariffForm();

        await loadTariffs();


        if (method === "POST") {

            alert(
                "Tariff added successfully."
            );

        } else {

            alert(
                "Tariff updated successfully."
            );
        }


    } catch (error) {

        console.error(
            "Error saving tariff:",
            error
        );
    }
}


/* -------------------------
   EDIT TARIFF
------------------------- */

async function editTariff(id) {

    try {

        await loadTariffStationDropdown();


        const response =
            await fetch(
                "/api/ev/tariffs/" + id
            );


        const tariff =
            await response.json();


        document.getElementById(
            "tariff-id"
        ).value =
            tariff.id;


        document.getElementById(
            "tariff-base-rate"
        ).value =
            tariff.baseRate;


        document.getElementById(
            "tariff-multiplier"
        ).value =
            tariff.peakMultiplier;


        document.getElementById(
            "tariff-start"
        ).value =
            formatTime(
                tariff.peakStartTime
            );


        document.getElementById(
            "tariff-end"
        ).value =
            formatTime(
                tariff.peakEndTime
            );


        if (tariff.station) {

            document.getElementById(
                "tariff-station"
            ).value =
                tariff.station.id;
        }


        document.getElementById(
            "tariff-form-title"
        ).innerText =
            "Edit Tariff";


        document.getElementById(
            "tariff-form-box"
        ).classList.remove("hidden");


    } catch (error) {

        console.error(
            "Error loading tariff:",
            error
        );
    }
}


/* -------------------------
   DELETE TARIFF
------------------------- */

async function deleteTariff(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this tariff?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/tariffs/" + id,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to delete tariff."
            );

            return;
        }


        alert(
            "Tariff deleted successfully."
        );


        await loadTariffs();


    } catch (error) {

        console.error(
            "Error deleting tariff:",
            error
        );
    }
}


/* =========================================================
   CHARGING SESSIONS
========================================================= */

async function loadSessions() {

    try {

        const response =
            await fetch("/api/ev/sessions");

        const sessions =
            await response.json();


        const tableBody =
            document.getElementById(
                "session-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        sessions.forEach(session => {

            const row =
                document.createElement("tr");


            const energy =
                Number(session.energyConsumed) > 0
                    ? session.energyConsumed + " kWh"
                    : "-";


            const rate =
                Number(session.rateApplied) > 0
                    ? "₹" +
                      Number(
                          session.rateApplied
                      ).toFixed(2)
                    : "-";


            const amount =
                Number(session.totalAmount) > 0
                    ? "₹" +
                      Number(
                          session.totalAmount
                      ).toFixed(2)
                    : "-";


            const isActive =
    session.status &&
    session.status.toUpperCase()
        === "ACTIVE";


let action =
    `<span>Completed</span>`;


if (isActive) {

    if (currentUserRole === "DRIVER") {

        action = `
            <button
                class="stop-btn"
                onclick="stopChargingSession(${session.id})">

                Stop

            </button>
        `;

    } else {

        action =
            `<span>Active</span>`;
    }
}


            row.innerHTML = `

                <td>${session.id}</td>

                <td>${session.driverName}</td>

                <td>${session.vehicleNumber}</td>

                <td>
                    ${
                        session.charger
                            ? session.charger.chargerName
                            : "-"
                    }
                </td>

                <td>
                    ${formatDateTime(
                        session.startTime
                    )}
                </td>

                <td>${energy}</td>

                <td>${rate}</td>

                <td>${amount}</td>

                <td>

                    <span class="status-badge
                        ${
                            isActive
                                ? "occupied"
                                : "available"
                        }">

                        ${session.status}

                    </span>

                </td>

                <td>${action}</td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading sessions:",
            error
        );
    }
}


/* -------------------------
   SHOW SESSION FORM
------------------------- */

async function showSessionForm() {

    await loadAvailableChargerDropdown();

    await loadSessionTariffDropdown();


    document.getElementById(
        "session-driver"
    ).value = "";


    document.getElementById(
        "session-vehicle"
    ).value = "";


    document.getElementById(
        "session-form-box"
    ).classList.remove("hidden");
}


/* -------------------------
   HIDE SESSION FORM
------------------------- */

function hideSessionForm() {

    document.getElementById(
        "session-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   AVAILABLE CHARGERS DROPDOWN
------------------------- */

async function loadAvailableChargerDropdown() {

    try {

        const response =
            await fetch("/api/ev/chargers");

        const chargers =
            await response.json();


        const availableChargers =
            chargers.filter(charger =>

                charger.status &&

                charger.status.toUpperCase()
                    === "AVAILABLE"
            );


        const select =
            document.getElementById(
                "session-charger"
            );


        select.innerHTML =
            `<option value="">Select Charger</option>`;


        availableChargers.forEach(charger => {

            const option =
                document.createElement("option");


            option.value =
                charger.id;


            option.textContent =
                charger.chargerName
                + " - "
                + charger.chargerType
                + " - "
                + (
                    charger.station
                        ? charger.station.stationName
                        : ""
                );


            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading available chargers:",
            error
        );
    }
}


/* -------------------------
   SESSION TARIFF DROPDOWN
------------------------- */

async function loadSessionTariffDropdown() {

    try {

        const response =
            await fetch("/api/ev/tariffs");

        const tariffs =
            await response.json();


        const select =
            document.getElementById(
                "session-tariff"
            );


        select.innerHTML =
            `<option value="">Select Tariff</option>`;


        tariffs.forEach(tariff => {

            const option =
                document.createElement("option");


            option.value =
                tariff.id;


            option.textContent =
                (
                    tariff.station
                        ? tariff.station.stationName
                        : "Station"
                )
                + " - ₹"
                + tariff.baseRate
                + "/kWh";


            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading session tariffs:",
            error
        );
    }
}


/* -------------------------
   START CHARGING SESSION
------------------------- */

async function startChargingSession() {

    const driverName =
        document.getElementById(
            "session-driver"
        ).value.trim();


    const vehicleNumber =
        document.getElementById(
            "session-vehicle"
        ).value.trim();


    const chargerId =
        document.getElementById(
            "session-charger"
        ).value;


    const tariffId =
        document.getElementById(
            "session-tariff"
        ).value;


    if (
        driverName === "" ||
        vehicleNumber === "" ||
        chargerId === "" ||
        tariffId === ""
    ) {

        alert(
            "Please fill all charging session details."
        );

        return;
    }


    const session = {

        driverName: driverName,

        vehicleNumber: vehicleNumber,

        charger: {
            id: Number(chargerId)
        },

        tariff: {
            id: Number(tariffId)
        }
    };


    try {

        const response =
            await fetch(
                "/api/ev/sessions",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(session)
                }
            );


        if (!response.ok) {

            alert(
                "Unable to start charging session."
            );

            return;
        }


        const text =
            await response.text();


        if (!text) {

            alert(
                "Charging session could not be started. Charger may not be available."
            );

            return;
        }


        hideSessionForm();


        await loadSessions();

        await loadDashboard();


        alert(
            "Charging session started successfully."
        );


    } catch (error) {

        console.error(
            "Error starting charging session:",
            error
        );
    }
}


/* -------------------------
   STOP CHARGING SESSION
------------------------- */

async function stopChargingSession(id) {

    const energy =
        prompt(
            "Enter energy consumed in kWh:"
        );


    if (energy === null) {
        return;
    }


    const energyValue =
        Number(energy);


    if (
        isNaN(energyValue) ||
        energyValue <= 0
    ) {

        alert(
            "Please enter a valid energy value."
        );

        return;
    }


    try {

        const response =
            await fetch(

                "/api/ev/sessions/"
                + id
                + "/stop?energyConsumed="
                + encodeURIComponent(
                    energyValue
                ),

                {
                    method: "PUT"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to stop charging session."
            );

            return;
        }


        const text =
            await response.text();


        if (!text) {

            alert(
                "Session is already completed or does not exist."
            );

            return;
        }


        const session =
            JSON.parse(text);


        alert(
            "Charging completed!\n\n"
            + "Energy: "
            + session.energyConsumed
            + " kWh\n"
            + "Rate: ₹"
            + Number(
                session.rateApplied
            ).toFixed(2)
            + "/kWh\n"
            + "Total Amount: ₹"
            + Number(
                session.totalAmount
            ).toFixed(2)
        );


        await loadSessions();

        await loadDashboard();


    } catch (error) {

        console.error(
            "Error stopping charging session:",
            error
        );
    }
}


/* =========================================================
   INVOICES
========================================================= */

async function loadInvoices() {

    try {

        const response =
            await fetch("/api/ev/invoices");


        if (!response.ok) {

            throw new Error(
                "Unable to load invoices"
            );
        }


        const invoices =
            await response.json();


        const tableBody =
            document.getElementById(
                "invoice-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        invoices.forEach(invoice => {

            const session =
                invoice.chargingSession;


            const row =
                document.createElement("tr");


            const isPaid =
                invoice.paymentStatus &&
                invoice.paymentStatus.toUpperCase()
                    === "PAID";


            row.innerHTML = `

                <td>${invoice.id}</td>

                <td>
                    ${
                        session
                            ? "#" + session.id
                            : "-"
                    }
                </td>

                <td>
                    ${
                        session
                            ? session.driverName
                            : "-"
                    }
                </td>

                <td>
                    ${
                        session
                            ? session.vehicleNumber
                            : "-"
                    }
                </td>

                <td>
                    ${formatDateTime(
                        invoice.invoiceDate
                    )}
                </td>

                <td>
                    ₹${Number(
                        invoice.amount || 0
                    ).toFixed(2)}
                </td>

                <td>

                    <span class="status-badge
                        ${
                            isPaid
                                ? "available"
                                : "maintenance"
                        }">

                        ${invoice.paymentStatus}

                    </span>

                </td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading invoices:",
            error
        );
    }
}


/* -------------------------
   SHOW INVOICE FORM
------------------------- */

async function showInvoiceForm() {

    console.log(
        "Generate Invoice button clicked"
    );


    /*
       IMPORTANT:
       Open the form FIRST.
       Then load the sessions.
    */

    document.getElementById(
        "invoice-form-box"
    ).classList.remove("hidden");


    await loadCompletedSessionDropdown();
}


/* -------------------------
   HIDE INVOICE FORM
------------------------- */

function hideInvoiceForm() {

    document.getElementById(
        "invoice-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   COMPLETED SESSION DROPDOWN
------------------------- */

async function loadCompletedSessionDropdown() {

    try {

        const sessionResponse =
            await fetch("/api/ev/sessions");


        const invoiceResponse =
            await fetch("/api/ev/invoices");


        if (!sessionResponse.ok) {

            throw new Error(
                "Unable to load charging sessions"
            );
        }


        if (!invoiceResponse.ok) {

            throw new Error(
                "Unable to load invoices"
            );
        }


        const sessions =
            await sessionResponse.json();


        const invoices =
            await invoiceResponse.json();


        console.log(
            "Charging sessions:",
            sessions
        );


        console.log(
            "Invoices:",
            invoices
        );


        /*
           Collect session IDs that already
           have invoices.
        */

        const invoicedSessionIds =
            invoices
                .filter(invoice =>
                    invoice.chargingSession
                )
                .map(invoice =>
                    invoice.chargingSession.id
                );


        /*
           Only completed sessions that
           don't already have an invoice.
        */

        const availableSessions =
            sessions.filter(session =>

                session.status &&

                session.status.toUpperCase()
                    === "COMPLETED"

                &&

                !invoicedSessionIds.includes(
                    session.id
                )
            );


        console.log(
            "Sessions available for invoice:",
            availableSessions
        );


        const select =
            document.getElementById(
                "invoice-session"
            );


        select.innerHTML =
            `<option value="">
                Select Completed Session
             </option>`;


        availableSessions.forEach(session => {

            const option =
                document.createElement("option");


            option.value =
                session.id;


            option.textContent =
                "#"
                + session.id
                + " - "
                + session.driverName
                + " - "
                + session.vehicleNumber
                + " - ₹"
                + Number(
                    session.totalAmount || 0
                ).toFixed(2);


            select.appendChild(option);
        });


        if (availableSessions.length === 0) {

            const option =
                document.createElement("option");


            option.disabled =
                true;


            option.textContent =
                "No completed sessions available";


            select.appendChild(option);
        }


    } catch (error) {

        console.error(
            "Error loading completed sessions:",
            error
        );


        alert(
            "Unable to load completed charging sessions."
        );
    }
}


/* -------------------------
   GENERATE INVOICE
------------------------- */

async function generateInvoice() {

    const sessionId =
        document.getElementById(
            "invoice-session"
        ).value;


    if (sessionId === "") {

        alert(
            "Please select a completed charging session."
        );

        return;
    }


    try {

        const response =
            await fetch(

                "/api/ev/invoices/session/"
                + sessionId,

                {
                    method: "POST"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to generate invoice."
            );

            return;
        }


        const text =
            await response.text();


        if (!text) {

            alert(
                "Invoice already exists or the session is not completed."
            );

            return;
        }


        const invoice =
            JSON.parse(text);


        hideInvoiceForm();


        await loadInvoices();


        alert(
            "Invoice generated successfully!\n\n"
            + "Invoice ID: "
            + invoice.id
            + "\nAmount: ₹"
            + Number(
                invoice.amount
            ).toFixed(2)
        );


    } catch (error) {

        console.error(
            "Error generating invoice:",
            error
        );


        alert(
            "An error occurred while generating the invoice."
        );
    }
}


/* =========================================================
   PAYMENTS
========================================================= */

async function loadPayments() {

    try {

        const response =
            await fetch("/api/ev/payments");


        if (!response.ok) {

            throw new Error(
                "Unable to load payments"
            );
        }


        const payments =
            await response.json();


        const tableBody =
            document.getElementById(
                "payment-table-body"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        payments.forEach(payment => {

            const invoice =
                payment.invoice;


            const session =
                invoice
                    ? invoice.chargingSession
                    : null;


            const row =
                document.createElement("tr");


            const isSuccess =
                payment.paymentStatus &&
                payment.paymentStatus.toUpperCase()
                    === "SUCCESS";


            row.innerHTML = `

                <td>${payment.id}</td>

                <td>
                    ${
                        invoice
                            ? "#" + invoice.id
                            : "-"
                    }
                </td>

                <td>
                    ${
                        session
                            ? session.driverName
                            : "-"
                    }
                </td>

                <td>
                    ₹${Number(
                        payment.amount || 0
                    ).toFixed(2)}
                </td>

                <td>
                    ${payment.paymentMethod || "-"}
                </td>

                <td>
                    ${formatDateTime(
                        payment.paymentDate
                    )}
                </td>

                <td>

                    <span class="status-badge
                        ${
                            isSuccess
                                ? "available"
                                : "maintenance"
                        }">

                        ${payment.paymentStatus}

                    </span>

                </td>
            `;


            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading payments:",
            error
        );
    }
}


/* -------------------------
   SHOW PAYMENT FORM
------------------------- */

async function showPaymentForm() {

    /*
       Show form first so even if API loading
       fails, the button visibly responds.
    */

    document.getElementById(
        "payment-form-box"
    ).classList.remove("hidden");


    await loadUnpaidInvoiceDropdown();
}


/* -------------------------
   HIDE PAYMENT FORM
------------------------- */

function hidePaymentForm() {

    document.getElementById(
        "payment-form-box"
    ).classList.add("hidden");
}


/* -------------------------
   UNPAID INVOICE DROPDOWN
------------------------- */

async function loadUnpaidInvoiceDropdown() {

    try {

        const response =
            await fetch("/api/ev/invoices");


        if (!response.ok) {

            throw new Error(
                "Unable to load invoices"
            );
        }


        const invoices =
            await response.json();


        const unpaidInvoices =
            invoices.filter(invoice =>

                invoice.paymentStatus &&

                invoice.paymentStatus.toUpperCase()
                    === "UNPAID"
            );


        const select =
            document.getElementById(
                "payment-invoice"
            );


        select.innerHTML =
            `<option value="">
                Select Unpaid Invoice
             </option>`;


        unpaidInvoices.forEach(invoice => {

            const option =
                document.createElement("option");


            option.value =
                invoice.id;


            const driver =
                invoice.chargingSession
                    ? invoice.chargingSession.driverName
                    : "";


            option.textContent =
                "Invoice #"
                + invoice.id
                + " - "
                + driver
                + " - ₹"
                + Number(
                    invoice.amount || 0
                ).toFixed(2);


            select.appendChild(option);
        });


        if (unpaidInvoices.length === 0) {

            const option =
                document.createElement("option");


            option.disabled =
                true;


            option.textContent =
                "No unpaid invoices available";


            select.appendChild(option);
        }


    } catch (error) {

        console.error(
            "Error loading unpaid invoices:",
            error
        );


        alert(
            "Unable to load unpaid invoices."
        );
    }
}


/* -------------------------
   MAKE PAYMENT
------------------------- */

async function makePayment() {

    const invoiceId =
        document.getElementById(
            "payment-invoice"
        ).value;


    const paymentMethod =
        document.getElementById(
            "payment-method"
        ).value;


    if (invoiceId === "") {

        alert(
            "Please select an unpaid invoice."
        );

        return;
    }


    try {

        const response =
            await fetch(

                "/api/ev/payments/invoice/"
                + invoiceId
                + "?paymentMethod="
                + encodeURIComponent(
                    paymentMethod
                ),

                {
                    method: "POST"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to process payment."
            );

            return;
        }


        const text =
            await response.text();


        if (!text) {

            alert(
                "This invoice is already paid."
            );

            return;
        }


        const payment =
            JSON.parse(text);


        hidePaymentForm();


        await loadPayments();

        await loadDashboard();


        alert(
            "Payment successful!\n\n"
            + "Payment ID: "
            + payment.id
            + "\nAmount: ₹"
            + Number(
                payment.amount
            ).toFixed(2)
            + "\nMethod: "
            + payment.paymentMethod
        );


    } catch (error) {

        console.error(
            "Error processing payment:",
            error
        );


        alert(
            "An error occurred while processing the payment."
        );
    }
}

/* =========================================================
   UTILITY EXPENSES
========================================================= */

async function loadExpenses() {

    try {

        const response =
            await fetch("/api/ev/expenses");

        if (!response.ok) {
            throw new Error("Unable to load expenses");
        }

        const expenses =
            await response.json();

        const tableBody =
            document.getElementById(
                "expense-table-body"
            );

        if (!tableBody) {
            return;
        }

        tableBody.innerHTML = "";


        expenses.forEach(expense => {

            const row =
                document.createElement("tr");

            const isPaid =
                expense.paymentStatus &&
                expense.paymentStatus.toUpperCase()
                    === "PAID";


            row.innerHTML = `

                <td>${expense.id}</td>

                <td>
                    ${
                        expense.station
                            ? expense.station.stationName
                            : "-"
                    }
                </td>

                <td>${expense.expenseType || "-"}</td>

                <td>${expense.providerName || "-"}</td>

                <td>
                    ₹${Number(
                        expense.amount || 0
                    ).toFixed(2)}
                </td>

                <td>${expense.expenseDate || "-"}</td>

                <td>${expense.paymentMethod || "-"}</td>

                <td>${expense.referenceNumber || "-"}</td>

                <td>

                    <span class="status-badge
                        ${
                            isPaid
                                ? "available"
                                : "maintenance"
                        }">

                        ${expense.paymentStatus || "-"}

                    </span>

                </td>

            <td>

    ${
        currentUserRole === "ADMIN" ||
        currentUserRole === "ACCOUNTANT"

        ? `
            <button
                class="edit-btn"
                onclick="editExpense(${expense.id})">

                Edit

            </button>

            <button
                class="delete-btn"
                onclick="deleteExpense(${expense.id})">

                Delete

            </button>
          `

        : "-"
    }

</td>
            `;

            tableBody.appendChild(row);
        });


    } catch (error) {

        console.error(
            "Error loading expenses:",
            error
        );
    }
}


/* ---------------------------------------------------------
   LOAD STATIONS FOR EXPENSE
--------------------------------------------------------- */

async function loadExpenseStationDropdown() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();

        const select =
            document.getElementById(
                "expense-station"
            );

        select.innerHTML =
            `<option value="">Select Station</option>`;


        stations.forEach(station => {

            const option =
                document.createElement("option");

            option.value =
                station.id;

            option.textContent =
                station.stationName
                + " - "
                + station.location;

            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading stations:",
            error
        );
    }
}


/* ---------------------------------------------------------
   SHOW EXPENSE FORM
--------------------------------------------------------- */

async function showExpenseForm() {

    await loadExpenseStationDropdown();


    document.getElementById(
        "expense-form-title"
    ).innerText =
        "Add Utility Expense";


    document.getElementById(
        "expense-id"
    ).value = "";


    document.getElementById(
        "expense-station"
    ).value = "";


    document.getElementById(
        "expense-type"
    ).value =
        "ELECTRICITY BILL";


    document.getElementById(
        "expense-provider"
    ).value = "";


    document.getElementById(
        "expense-amount"
    ).value = "";


    document.getElementById(
        "expense-date"
    ).value =
        new Date().toISOString().split("T")[0];


    document.getElementById(
        "expense-status"
    ).value =
        "UNPAID";


    document.getElementById(
        "expense-payment-method"
    ).value = "";


    document.getElementById(
        "expense-reference"
    ).value = "";


    document.getElementById(
        "expense-form-box"
    ).classList.remove("hidden");
}


/* ---------------------------------------------------------
   HIDE EXPENSE FORM
--------------------------------------------------------- */

function hideExpenseForm() {

    document.getElementById(
        "expense-form-box"
    ).classList.add("hidden");
}


/* ---------------------------------------------------------
   SAVE / UPDATE EXPENSE
--------------------------------------------------------- */

async function saveExpense() {

    const id =
        document.getElementById(
            "expense-id"
        ).value;


    const stationId =
        document.getElementById(
            "expense-station"
        ).value;


    const expenseType =
        document.getElementById(
            "expense-type"
        ).value;


    const providerName =
        document.getElementById(
            "expense-provider"
        ).value.trim();


    const amount =
        document.getElementById(
            "expense-amount"
        ).value;


    const expenseDate =
        document.getElementById(
            "expense-date"
        ).value;


    const paymentStatus =
        document.getElementById(
            "expense-status"
        ).value;


    const paymentMethod =
        document.getElementById(
            "expense-payment-method"
        ).value;


    const referenceNumber =
        document.getElementById(
            "expense-reference"
        ).value.trim();


    if (
        stationId === "" ||
        providerName === "" ||
        amount === "" ||
        expenseDate === ""
    ) {

        alert(
            "Please fill all required expense details."
        );

        return;
    }


    if (Number(amount) <= 0) {

        alert(
            "Expense amount must be greater than 0."
        );

        return;
    }


    if (
        paymentStatus === "PAID" &&
        paymentMethod === ""
    ) {

        alert(
            "Please select a payment method for a paid expense."
        );

        return;
    }


    const expense = {

        expenseType: expenseType,

        providerName: providerName,

        amount: Number(amount),

        expenseDate: expenseDate,

        paymentMethod:
            paymentMethod === ""
                ? null
                : paymentMethod,

        referenceNumber:
            referenceNumber === ""
                ? null
                : referenceNumber,

        paymentStatus: paymentStatus,

        station: {
            id: Number(stationId)
        }
    };


    let url =
        "/api/ev/expenses";

    let method =
        "POST";


    if (id !== "") {

        url =
            "/api/ev/expenses/" + id;

        method =
            "PUT";
    }


    try {

        const response =
            await fetch(url, {

                method: method,

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(expense)
            });


        if (!response.ok) {

            alert(
                "Unable to save utility expense."
            );

            return;
        }


        hideExpenseForm();

        await loadExpenses();


        if (method === "POST") {

            alert(
                "Utility expense added successfully."
            );

        } else {

            alert(
                "Utility expense updated successfully."
            );
        }


    } catch (error) {

        console.error(
            "Error saving expense:",
            error
        );
    }
}


/* ---------------------------------------------------------
   EDIT EXPENSE
--------------------------------------------------------- */

async function editExpense(id) {

    try {

        await loadExpenseStationDropdown();


        const response =
            await fetch(
                "/api/ev/expenses/" + id
            );


        if (!response.ok) {

            alert(
                "Unable to load expense."
            );

            return;
        }


        const expense =
            await response.json();


        document.getElementById(
            "expense-id"
        ).value =
            expense.id;


        if (expense.station) {

            document.getElementById(
                "expense-station"
            ).value =
                expense.station.id;
        }


        document.getElementById(
            "expense-type"
        ).value =
            expense.expenseType;


        document.getElementById(
            "expense-provider"
        ).value =
            expense.providerName || "";


        document.getElementById(
            "expense-amount"
        ).value =
            expense.amount;


        document.getElementById(
            "expense-date"
        ).value =
            expense.expenseDate;


        document.getElementById(
            "expense-status"
        ).value =
            expense.paymentStatus;


        document.getElementById(
            "expense-payment-method"
        ).value =
            expense.paymentMethod || "";


        document.getElementById(
            "expense-reference"
        ).value =
            expense.referenceNumber || "";


        document.getElementById(
            "expense-form-title"
        ).innerText =
            "Edit Utility Expense";


        document.getElementById(
            "expense-form-box"
        ).classList.remove("hidden");


    } catch (error) {

        console.error(
            "Error loading expense:",
            error
        );
    }
}


/* ---------------------------------------------------------
   DELETE EXPENSE
--------------------------------------------------------- */

async function deleteExpense(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this utility expense?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/expenses/" + id,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to delete utility expense."
            );

            return;
        }


        alert(
            "Utility expense deleted successfully."
        );


        await loadExpenses();


    } catch (error) {

        console.error(
            "Error deleting expense:",
            error
        );
    }
}
/* =========================================================
   BUDGETS
========================================================= */

async function loadBudgets() {

    try {

        const response =
            await fetch("/api/ev/budgets");

        if (!response.ok) {
            throw new Error("Unable to load budgets");
        }

        const budgets =
            await response.json();

        const tableBody =
            document.getElementById(
                "budget-table-body"
            );

        if (!tableBody) {
            return;
        }

        tableBody.innerHTML = "";


        for (const budget of budgets) {

            let remaining =
                Number(budget.budgetAmount || 0)
                -
                Number(budget.spentAmount || 0);


            /*
             Try to get remaining amount
             from backend endpoint.
            */

            try {

                const remainingResponse =
                    await fetch(
                        "/api/ev/budgets/"
                        + budget.id
                        + "/remaining"
                    );

                if (remainingResponse.ok) {

                    const value =
                        await remainingResponse.text();

                    if (value !== "") {
                        remaining = Number(value);
                    }
                }

            } catch (error) {

                console.error(
                    "Error loading remaining budget:",
                    error
                );
            }


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>${budget.id}</td>

                <td>${budget.budgetName}</td>

                <td>
                    ${
                        budget.station
                            ? budget.station.stationName
                            : "-"
                    }
                </td>

                <td>
                    ₹${Number(
                        budget.budgetAmount || 0
                    ).toFixed(2)}
                </td>

                <td>
                    ₹${Number(
                        budget.spentAmount || 0
                    ).toFixed(2)}
                </td>

                <td>
                    <strong>
                        ₹${Number(
                            remaining
                        ).toFixed(2)}
                    </strong>
                </td>

                <td>${budget.startDate || "-"}</td>

                <td>${budget.endDate || "-"}</td>

                <td>

                    <div class="action-buttons">

                        <button
                            class="edit-btn"
                            onclick="editBudget(${budget.id})">
                            Edit
                        </button>

                        <button
                            class="delete-btn"
                            onclick="deleteBudget(${budget.id})">
                            Delete
                        </button>

                    </div>

                </td>
            `;


            tableBody.appendChild(row);
        }


    } catch (error) {

        console.error(
            "Error loading budgets:",
            error
        );
    }
}


/* =========================================================
   LOAD STATIONS FOR BUDGET
========================================================= */

async function loadBudgetStationDropdown() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();

        const select =
            document.getElementById(
                "budget-station"
            );

        select.innerHTML =
            `<option value="">Select Station</option>`;


        stations.forEach(station => {

            const option =
                document.createElement("option");

            option.value =
                station.id;

            option.textContent =
                station.stationName
                + " - "
                + station.location;

            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading budget stations:",
            error
        );
    }
}


/* =========================================================
   SHOW BUDGET FORM
========================================================= */

async function showBudgetForm() {

    await loadBudgetStationDropdown();


    document.getElementById(
        "budget-form-title"
    ).innerText =
        "Add Budget";


    document.getElementById(
        "budget-id"
    ).value = "";


    document.getElementById(
        "budget-station"
    ).value = "";


    document.getElementById(
        "budget-name"
    ).value = "";


    document.getElementById(
        "budget-amount"
    ).value = "";


    document.getElementById(
        "budget-spent"
    ).value = "0";


    document.getElementById(
        "budget-start"
    ).value = "";


    document.getElementById(
        "budget-end"
    ).value = "";


    document.getElementById(
        "budget-form-box"
    ).classList.remove("hidden");
}


/* =========================================================
   HIDE BUDGET FORM
========================================================= */

function hideBudgetForm() {

    document.getElementById(
        "budget-form-box"
    ).classList.add("hidden");
}


/* =========================================================
   SAVE / UPDATE BUDGET
========================================================= */

async function saveBudget() {

    const id =
        document.getElementById(
            "budget-id"
        ).value;


    const stationId =
        document.getElementById(
            "budget-station"
        ).value;


    const budgetName =
        document.getElementById(
            "budget-name"
        ).value.trim();


    const budgetAmount =
        document.getElementById(
            "budget-amount"
        ).value;


    const spentAmount =
        document.getElementById(
            "budget-spent"
        ).value;


    const startDate =
        document.getElementById(
            "budget-start"
        ).value;


    const endDate =
        document.getElementById(
            "budget-end"
        ).value;


    if (
        stationId === "" ||
        budgetName === "" ||
        budgetAmount === "" ||
        spentAmount === "" ||
        startDate === "" ||
        endDate === ""
    ) {

        alert(
            "Please fill all budget details."
        );

        return;
    }


    if (Number(budgetAmount) <= 0) {

        alert(
            "Budget amount must be greater than 0."
        );

        return;
    }


    if (Number(spentAmount) < 0) {

        alert(
            "Spent amount cannot be negative."
        );

        return;
    }


    if (
        Number(spentAmount) >
        Number(budgetAmount)
    ) {

        alert(
            "Spent amount cannot be greater than the budget amount."
        );

        return;
    }


    if (endDate < startDate) {

        alert(
            "End date cannot be before start date."
        );

        return;
    }


    const budget = {

        budgetName: budgetName,

        budgetAmount:
            Number(budgetAmount),

        spentAmount:
            Number(spentAmount),

        startDate: startDate,

        endDate: endDate,

        station: {
            id: Number(stationId)
        }
    };


    let url =
        "/api/ev/budgets";

    let method =
        "POST";


    if (id !== "") {

        url =
            "/api/ev/budgets/" + id;

        method =
            "PUT";
    }


    try {

        const response =
            await fetch(url, {

                method: method,

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(budget)
            });


        if (!response.ok) {

            alert(
                "Unable to save budget."
            );

            return;
        }


        hideBudgetForm();

        await loadBudgets();


        if (method === "POST") {

            alert(
                "Budget added successfully."
            );

        } else {

            alert(
                "Budget updated successfully."
            );
        }


    } catch (error) {

        console.error(
            "Error saving budget:",
            error
        );
    }
}


/* =========================================================
   EDIT BUDGET
========================================================= */

async function editBudget(id) {

    try {

        await loadBudgetStationDropdown();


        const response =
            await fetch(
                "/api/ev/budgets/" + id
            );


        if (!response.ok) {

            alert(
                "Unable to load budget."
            );

            return;
        }


        const budget =
            await response.json();


        document.getElementById(
            "budget-id"
        ).value =
            budget.id;


        document.getElementById(
            "budget-name"
        ).value =
            budget.budgetName;


        document.getElementById(
            "budget-amount"
        ).value =
            budget.budgetAmount;


        document.getElementById(
            "budget-spent"
        ).value =
            budget.spentAmount;


        document.getElementById(
            "budget-start"
        ).value =
            budget.startDate;


        document.getElementById(
            "budget-end"
        ).value =
            budget.endDate;


        if (budget.station) {

            document.getElementById(
                "budget-station"
            ).value =
                budget.station.id;
        }


        document.getElementById(
            "budget-form-title"
        ).innerText =
            "Edit Budget";


        document.getElementById(
            "budget-form-box"
        ).classList.remove("hidden");


    } catch (error) {

        console.error(
            "Error loading budget:",
            error
        );
    }
}


/* =========================================================
   DELETE BUDGET
========================================================= */

async function deleteBudget(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this budget?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/budgets/" + id,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            alert(
                "Unable to delete budget."
            );

            return;
        }


        alert(
            "Budget deleted successfully."
        );


        await loadBudgets();


    } catch (error) {

        console.error(
            "Error deleting budget:",
            error
        );
    }
}

/* =========================================================
   REPORTS
========================================================= */

async function loadReports() {

    await loadProfitLoss();

    await loadReportStationDropdown();

    await loadReportBudgetDropdown();
}


/* =========================================================
   PROFIT AND LOSS
========================================================= */

async function loadProfitLoss() {

    try {

        const response =
            await fetch(
                "/api/ev/reports/profit-loss"
            );

        if (!response.ok) {
            throw new Error(
                "Unable to load profit and loss report"
            );
        }

        const report =
            await response.json();


        document.getElementById(
            "report-revenue"
        ).innerText =
            "₹" +
            Number(
                report.totalRevenue || 0
            ).toFixed(2);


        document.getElementById(
            "report-expenses"
        ).innerText =
            "₹" +
            Number(
                report.totalExpenses || 0
            ).toFixed(2);


        document.getElementById(
            "report-profit"
        ).innerText =
            "₹" +
            Number(
                report.profitOrLoss || 0
            ).toFixed(2);


        document.getElementById(
            "report-result"
        ).innerText =
            report.result || "-";


    } catch (error) {

        console.error(
            "Error loading P&L report:",
            error
        );
    }
}


/* =========================================================
   SITE DROPDOWN
========================================================= */

async function loadReportStationDropdown() {

    try {

        const response =
            await fetch("/api/ev/stations");

        const stations =
            await response.json();

        const select =
            document.getElementById(
                "report-station"
            );

        select.innerHTML =
            `<option value="">
                Select Station
             </option>`;


        stations.forEach(station => {

            const option =
                document.createElement(
                    "option"
                );

            option.value =
                station.id;

            option.textContent =
                station.stationName
                + " - "
                + station.location;

            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading report stations:",
            error
        );
    }
}


/* =========================================================
   SITE YIELD REPORT
========================================================= */

async function loadSiteYield() {

    const stationId =
        document.getElementById(
            "report-station"
        ).value;


    if (stationId === "") {

        alert(
            "Please select a station."
        );

        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/reports/site-yield/"
                + stationId
            );


        if (!response.ok) {

            alert(
                "Unable to generate site yield report."
            );

            return;
        }


        const report =
            await response.json();


        document.getElementById(
            "yield-station"
        ).innerText =
            report.stationName;


        document.getElementById(
            "yield-sessions"
        ).innerText =
            report.completedSessions;


        document.getElementById(
            "yield-energy"
        ).innerText =
            Number(
                report.totalEnergyDelivered || 0
            ).toFixed(2)
            + " kWh";


        document.getElementById(
            "yield-revenue"
        ).innerText =
            "₹" +
            Number(
                report.chargingRevenue || 0
            ).toFixed(2);


        document.getElementById(
            "yield-expenses"
        ).innerText =
            "₹" +
            Number(
                report.utilityExpenses || 0
            ).toFixed(2);


        document.getElementById(
            "yield-net"
        ).innerText =
            "₹" +
            Number(
                report.netYield || 0
            ).toFixed(2);


        document.getElementById(
            "site-yield-result"
        ).classList.remove(
            "hidden"
        );


    } catch (error) {

        console.error(
            "Error loading site yield:",
            error
        );
    }
}


/* =========================================================
   BUDGET DROPDOWN
========================================================= */

async function loadReportBudgetDropdown() {

    try {

        const response =
            await fetch("/api/ev/budgets");

        const budgets =
            await response.json();


        const select =
            document.getElementById(
                "report-budget"
            );


        select.innerHTML =
            `<option value="">
                Select Budget
             </option>`;


        budgets.forEach(budget => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                budget.id;


            option.textContent =
                budget.budgetName
                + " - ₹"
                + Number(
                    budget.budgetAmount || 0
                ).toFixed(2);


            select.appendChild(option);
        });


    } catch (error) {

        console.error(
            "Error loading report budgets:",
            error
        );
    }
}


/* =========================================================
   BUDGET REPORT
========================================================= */

async function loadBudgetReport() {

    const budgetId =
        document.getElementById(
            "report-budget"
        ).value;


    if (budgetId === "") {

        alert(
            "Please select a budget."
        );

        return;
    }


    try {

        const response =
            await fetch(
                "/api/ev/reports/budget/"
                + budgetId
            );


        if (!response.ok) {

            alert(
                "Unable to generate budget report."
            );

            return;
        }


        const report =
            await response.json();


        document.getElementById(
            "report-budget-name"
        ).innerText =
            report.budgetName;


        document.getElementById(
            "report-budget-total"
        ).innerText =
            "₹" +
            Number(
                report.budgetAmount || 0
            ).toFixed(2);


        document.getElementById(
            "report-budget-spent"
        ).innerText =
            "₹" +
            Number(
                report.spentAmount || 0
            ).toFixed(2);


        document.getElementById(
            "report-budget-remaining"
        ).innerText =
            "₹" +
            Number(
                report.remainingAmount || 0
            ).toFixed(2);


        document.getElementById(
            "report-budget-percent"
        ).innerText =
            Number(
                report.usedPercentage || 0
            ).toFixed(2)
            + "%";


        document.getElementById(
            "budget-report-result"
        ).classList.remove(
            "hidden"
        );


    } catch (error) {

        console.error(
            "Error loading budget report:",
            error
        );
    }
}