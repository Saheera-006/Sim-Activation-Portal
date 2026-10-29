
const API = "http://localhost:8080";

const messageBox = document.getElementById("message");
const offersBox = document.getElementById("offers");

const completedSteps = new Set();

function updateProgress(stepNumber) {
    completedSteps.add(stepNumber);

    document.querySelectorAll(".progress-step").forEach(step => {
        const number = Number(step.dataset.step);
        const status = step.querySelector(".step-status");
        const circle = step.querySelector(".step-number");

        if (completedSteps.has(number)) {
            step.classList.add("completed");
            status.textContent = "Completed";
            circle.textContent = "✓";
        }
    });

    const count = completedSteps.size;

    document.getElementById("progressText").textContent =
        `${count} of 5 steps completed`;

    document.getElementById("progressBar").style.width =
        `${(count / 5) * 100}%`;
}

function showMessage(message, success = true) {
    messageBox.textContent = message;
    messageBox.className = `message ${success ? "success" : "error"}`;
    messageBox.scrollIntoView({ behavior: "smooth", block: "center" });
}

document.querySelectorAll("form input[pattern]").forEach(input => {
    input.addEventListener("input", () => {
        input.setCustomValidity("");
    });
});

async function sendRequest(url, method, data) {
    const response = await fetch(`${API}${url}`, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    const text = await response.text();
    let result;

    try {
        result = text ? JSON.parse(text) : {};
    } catch {
        result = { message: text };
    }

    if (!response.ok) {
        const error = new Error(
            result.message || result.error || `Request failed (${response.status})`
        );
        error.status = response.status;
        throw error;
    }

    return result;
}

function handleForm(formId, url, method, getData, onSuccess) {
    document.getElementById(formId).addEventListener("submit", async event => {
        event.preventDefault();

        const form = event.currentTarget;

        if (!form.reportValidity()) {
            return;
        }

        const button = form.querySelector('button[type="submit"]');
        const originalText = button.textContent;

        button.disabled = true;
        button.textContent = "Please wait...";
        messageBox.className = "message hidden";

        const stepMap = {
            simForm: 1,
            basicForm: 2,
            personalForm: 3,
            addressForm: 4,
            idForm: 5
        };

        try {
            const result = await sendRequest(url, method, getData(form));
            const message = result.message || "Request completed successfully.";

            showMessage(message, true);

            if (stepMap[formId]) {
                updateProgress(stepMap[formId]);
            }

            if (onSuccess) {
                onSuccess(result);
            }
        } catch (error) {
            const isAlreadyActive =
                (error.message || "").toLowerCase().includes("sim already active");

            if (
                isAlreadyActive &&
                (formId === "simForm" || formId === "idForm")
            ) {
                updateProgress(stepMap[formId]);

                if (formId === "idForm") {
                    const heading = document.querySelector(".progress-card h2");
                    heading.textContent = "Activation Process — Completed";
                }

                showMessage(error.message, true);
            } else {
                showMessage(
                    error.message || "Unable to connect to the server.",
                    false
                );
            }
        } finally {
            button.disabled = false;
            button.textContent = originalText;
        }
    });
}

handleForm("simForm", "/api/sims/validate", "POST", form => ({
    simNumber: form.simNumber.value.trim(),
    serviceNumber: form.serviceNumber.value.trim()
}), result => {
    offersBox.innerHTML = "";
    offersBox.classList.remove("hidden");

    const heading = document.createElement("h3");
    heading.textContent = "Available Offers";
    offersBox.appendChild(heading);

    if (!Array.isArray(result) || result.length === 0) {
        const item = document.createElement("div");
        item.className = "offer-item";
        item.textContent = "No offers available.";
        offersBox.appendChild(item);
        return;
    }

    result.forEach(offer => {
        const item = document.createElement("div");
        item.className = "offer-item";

        const title = document.createElement("strong");
        title.textContent = offer.offerName || "SIM Offer";

        const details = document.createElement("p");
        details.textContent =
            `Cost: ₹${offer.cost} | Calls: ${offer.callQty} | ` +
            `Data: ${offer.dataQty} | Duration: ${offer.duration} days`;

        item.append(title, details);
        offersBox.appendChild(item);
    });
});

handleForm("basicForm", "/api/customers/basic-validation", "POST", form => ({
    email: form.email.value.trim(),
    dob: form.dob.value
}));

handleForm("personalForm", "/api/customers/personal-validation", "POST", form => ({
    firstName: form.firstName.value.trim(),
    lastName: form.lastName.value.trim(),
    confirmEmail: form.confirmEmail.value.trim()
}));

handleForm("addressForm", "/api/customers/address", "PUT", form => {
    const data = {
        address: form.address.value.trim(),
        city: form.city.value.trim(),
        pincode: form.pincode.value.trim(),
        state: form.state.value.trim()
    };

    if (form.addressId.value.trim()) {
        data.addressId = Number(form.addressId.value);
    }

    if (form.uniqueIdNumber.value.trim()) {
        data.uniqueIdNumber = form.uniqueIdNumber.value.trim();
    }

    return data;
});

handleForm("idForm", "/api/customers/id-validation", "POST", form => ({
    uniqueIdNumber: form.uniqueIdNumber.value.trim(),
    firstName: form.firstName.value.trim(),
    lastName: form.lastName.value.trim(),
    dateOfBirth: form.dateOfBirth.value
}), result => {
    const message = result.message || "";

    if (
        message.toLowerCase().includes("activated successfully") ||
        message.toLowerCase().includes("sim already active")
    ) {
        const heading = document.querySelector(".progress-card h2");
        heading.textContent = "Activation Process — Completed";
        showMessage(message, true);
    }
});
