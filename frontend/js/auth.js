// Get JWT Token
function getToken() {
    return localStorage.getItem("token");
}

// Check whether user is logged in
function checkAuth() {

    const token = getToken();

    if (!token) {
        window.location.href = "login.html";
    }
}

// Logout
function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";
}