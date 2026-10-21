
document.addEventListener("DOMContentLoaded", () => {
    checkAuth();

    // ==========================================
    // PROFILE ELEMENTS
    // ==========================================

    const fullNameInput = document.getElementById("fullName");
    const emailInput = document.getElementById("email");
    const saveProfileBtn = document.getElementById("saveProfileBtn");
    const cancelProfileBtn = document.getElementById("cancelProfileBtn");
    const profileMessage = document.getElementById("profileMessage");

    const profileImage = document.getElementById("profileImage");
    const imageInput = document.getElementById("imageInput");
    const changePhotoBtn = document.getElementById("changePhotoBtn");

    let originalProfile = {
        name: "",
        email: ""
    };

    let photoPreviewUrl = null;

    // ==========================================
    // PASSWORD ELEMENTS
    // ==========================================

    const changePasswordBtn = document.getElementById("changePasswordBtn");
    const passwordSection = document.getElementById("passwordSection");
    const savePasswordBtn = document.getElementById("savePasswordBtn");
    const cancelPasswordBtn = document.getElementById("cancelPasswordBtn");

    const currentPasswordInput = document.getElementById("currentPassword");
    const newPasswordInput = document.getElementById("newPassword");
    const confirmPasswordInput = document.getElementById("confirmPassword");

    // ==========================================
    // MESSAGE HANDLING
    // ==========================================

    function showMessage(message, isError = false) {
        if (!profileMessage) return;

        profileMessage.textContent = message;

        if (!message) {
            profileMessage.className = "hidden";
            return;
        }

        profileMessage.className = isError
            ? "mb-6 rounded-xl bg-red-100 p-4 text-sm text-red-700"
            : "mb-6 rounded-xl bg-green-100 p-4 text-sm text-green-700";
    }

    // ==========================================
    // API REQUEST HELPER
    // Supports JSON and plain-text responses
    // ==========================================

    async function apiRequest(url, options = {}) {
        const token = getToken();

        if (!token) {
            logout();
            throw new Error("Your session has expired. Please log in again.");
        }

        const response = await fetch(`${BASE_URL}${url}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
                ...(options.headers || {})
            }
        });

        const responseText = await response.text();

        if (response.status === 401 || response.status === 403) {
            throw new Error(
                "Authentication failed or access was denied. Please log in again if your session has expired."
            );
        }

        if (!response.ok) {
            let errorMessage = responseText || "Request failed.";

            // Extract a message if the backend returns JSON errors.
            try {
                const errorData = JSON.parse(responseText);
                errorMessage =
                    errorData.message ||
                    errorData.error ||
                    errorMessage;
            } catch {
                // The response is plain text, so keep it as-is.
            }

            throw new Error(errorMessage);
        }

        if (!responseText) {
            return null;
        }

        // Parse JSON when possible; otherwise return plain text.
        try {
            return JSON.parse(responseText);
        } catch {
            return responseText;
        }
    }

    // ==========================================
    // LOAD PROFILE
    // ==========================================

    async function loadProfile() {
        try {
            showMessage("Loading profile...");

            const profile = await apiRequest("/users/me");

            if (fullNameInput) {
                fullNameInput.value = profile.name || "";
            }

            if (emailInput) {
                emailInput.value = profile.email || "";
            }

            originalProfile = {
                name: profile.name || "",
                email: profile.email || ""
            };

            showMessage("");
        } catch (error) {
            showMessage(error.message, true);
        }
    }

    // ==========================================
    // SAVE PROFILE
    // ==========================================

    async function saveProfile() {
        if (!fullNameInput || !emailInput || !saveProfileBtn) {
            showMessage("Profile form elements could not be found.", true);
            return;
        }

        const name = fullNameInput.value.trim();
        const email = emailInput.value.trim();

        if (!name || !email) {
            showMessage("Name and email are required.", true);
            return;
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showMessage("Please enter a valid email address.", true);
            return;
        }

        saveProfileBtn.disabled = true;
        const originalButtonText = saveProfileBtn.textContent;
        saveProfileBtn.textContent = "Saving...";

        try {
            const updatedProfile = await apiRequest("/users/me", {
                method: "PUT",
                body: JSON.stringify({
                    name,
                    email
                })
            });

            fullNameInput.value = updatedProfile.name || name;
            emailInput.value = updatedProfile.email || email;

            originalProfile = {
                name: fullNameInput.value,
                email: emailInput.value
            };

            showMessage("Profile updated successfully!");
        } catch (error) {
            showMessage(error.message, true);
        } finally {
            saveProfileBtn.disabled = false;
            saveProfileBtn.textContent = originalButtonText;
        }
    }

    // ==========================================
    // CANCEL PROFILE CHANGES
    // ==========================================

    function cancelChanges() {
        if (fullNameInput) {
            fullNameInput.value = originalProfile.name;
        }

        if (emailInput) {
            emailInput.value = originalProfile.email;
        }

        showMessage("Unsaved profile changes discarded.");
    }

    if (saveProfileBtn) {
        saveProfileBtn.addEventListener("click", saveProfile);
    }

    if (cancelProfileBtn) {
        cancelProfileBtn.addEventListener("click", cancelChanges);
    }

    // ==========================================
    // PROFILE PHOTO PREVIEW
    // This does not permanently upload the photo.
    // ==========================================

    if (changePhotoBtn && imageInput) {
        changePhotoBtn.addEventListener("click", () => {
            imageInput.click();
        });
    }

    if (imageInput && profileImage) {
        imageInput.addEventListener("change", () => {
            const file = imageInput.files && imageInput.files[0];

            if (!file) return;

            const allowedTypes = [
                "image/png",
                "image/jpeg",
                "image/webp"
            ];

            if (!allowedTypes.includes(file.type)) {
                showMessage(
                    "Please select a PNG, JPG, or WebP image.",
                    true
                );
                imageInput.value = "";
                return;
            }

            if (file.size > 5 * 1024 * 1024) {
                showMessage(
                    "Image must be smaller than 5 MB.",
                    true
                );
                imageInput.value = "";
                return;
            }

            if (photoPreviewUrl) {
                URL.revokeObjectURL(photoPreviewUrl);
            }

            photoPreviewUrl = URL.createObjectURL(file);
            profileImage.src = photoPreviewUrl;

            showMessage(
                "Photo preview updated. It has not been saved permanently."
            );
        });
    }

    // ==========================================
    // SHOW / HIDE PASSWORD FORM
    // ==========================================

    if (changePasswordBtn && passwordSection) {
        changePasswordBtn.addEventListener("click", () => {
            passwordSection.classList.toggle("hidden");
            showMessage("");

            if (!passwordSection.classList.contains("hidden") &&
                currentPasswordInput) {
                currentPasswordInput.focus();
            }
        });
    }

    // ==========================================
    // CANCEL PASSWORD CHANGE
    // ==========================================

    if (cancelPasswordBtn && passwordSection) {
        cancelPasswordBtn.addEventListener("click", () => {
            if (currentPasswordInput) {
                currentPasswordInput.value = "";
            }

            if (newPasswordInput) {
                newPasswordInput.value = "";
            }

            if (confirmPasswordInput) {
                confirmPasswordInput.value = "";
            }

            passwordSection.classList.add("hidden");
            showMessage("");
        });
    }

    // ==========================================
    // UPDATE PASSWORD
    // ==========================================

    if (savePasswordBtn) {
        savePasswordBtn.addEventListener("click", async (event) => {
            event.preventDefault();

            if (
                !currentPasswordInput ||
                !newPasswordInput ||
                !confirmPasswordInput ||
                !passwordSection
            ) {
                showMessage("Password form elements could not be found.", true);
                return;
            }

            const currentPassword = currentPasswordInput.value;
            const newPassword = newPasswordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!currentPassword || !newPassword || !confirmPassword) {
                showMessage("Please fill in all password fields.", true);
                return;
            }

            if (newPassword.length < 6 || newPassword.length > 20) {
                showMessage(
                    "New password must be between 6 and 20 characters.",
                    true
                );
                return;
            }

            if (newPassword === currentPassword) {
                showMessage(
                    "Your new password must be different from your current password.",
                    true
                );
                return;
            }

            if (newPassword !== confirmPassword) {
                showMessage("New passwords do not match.", true);
                return;
            }

            const originalButtonText = savePasswordBtn.textContent;

            savePasswordBtn.disabled = true;
            savePasswordBtn.textContent = "Updating...";

            try {
                await apiRequest("/users/me/password", {
                    method: "PUT",
                    body: JSON.stringify({
                        currentPassword,
                        newPassword,
                        confirmPassword
                    })
                });

                currentPasswordInput.value = "";
                newPasswordInput.value = "";
                confirmPasswordInput.value = "";

                passwordSection.classList.add("hidden");

                showMessage(
                    "Password changed successfully! Use your new password the next time you log in."
                );
            } catch (error) {
                showMessage(
                    error.message || "Unable to change password.",
                    true
                );
            } finally {
                savePasswordBtn.disabled = false;
                savePasswordBtn.textContent = originalButtonText;
            }
        });
    }

    // ==========================================
    // INITIALIZE PROFILE
    // ==========================================

    loadProfile();
});
