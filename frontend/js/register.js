const registerForm = document.getElementById("registerForm");

registerForm.addEventListener("submit", async function (e) {

    e.preventDefault();

    const name = document.getElementById("fullName").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();

    // Validation
    if (!name || !email || !password || !confirmPassword) {
        alert("Please fill all fields.");
        return;
    }

    if (password !== confirmPassword) {
        alert("Passwords do not match.");
        return;
    }

    try {

        const response = await fetch(`${BASE_URL}/users/register`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                name,
                email,
                password
            })

        });

        const data = await response.json();

        if (response.ok) {

            alert("Registration successful!");

            window.location.href = "login.html";

        } else {

            alert(data.message || "Registration failed.");

        }

    } catch (error) {

        console.error(error);

        alert("Unable to connect to server.");

    }

});