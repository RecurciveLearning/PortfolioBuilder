document.addEventListener('DOMContentLoaded', function () {
    // ----- AOS Animation -----
    AOS.init({
        duration: 1000,
        once: true
    });

    // ----- Typed Text -----
    const typedElement = document.getElementById('typed-text');
    if (typedElement) {
        new Typed('#typed-text', {
            strings: ['Java Backend Developer', 'Spring Boot Expert', 'Microservices Architect', 'Problem Solver'],
            typeSpeed: 50,
            backSpeed: 30,
            loop: true
        });
    }

    // ----- Theme Toggle -----
    const themeToggle = document.getElementById('themeToggle');
    if (themeToggle) {
        const savedTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', savedTheme);
        themeToggle.querySelector('i').className = savedTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';

        themeToggle.addEventListener('click', function () {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            this.querySelector('i').className = newTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
        });
    }

    // ----- Contact Form -----
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const formData = {
                name: this.name.value,
                email: this.email.value,
                message: this.message.value
            };
            try {
                const response = await fetch('/contact', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(formData)
                });
                if (response.ok) {
                    alert('Message sent successfully!');
                    this.reset();
                } else {
                    alert('Error sending message. Please try again.');
                }
            } catch (error) {
                alert('Error sending message. Please try again.');
            }
        });
    }

    // ----- Meeting Form -----
    const meetingForm = document.getElementById('meetingForm');
    if (meetingForm) {
        meetingForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const formData = {
                name: this.name.value,
                email: this.email.value,
                phone: this.phone.value,
                preferredDatetime: this.preferredDatetime.value,
                type: this.type.value,
                message: this.message.value
            };
            try {
                const response = await fetch('/meeting/request', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(formData)
                });
                if (response.ok) {
                    alert('Meeting request submitted successfully!');
                    this.reset();
                } else {
                    alert('Error submitting meeting request. Please try again.');
                }
            } catch (error) {
                alert('Error submitting meeting request. Please try again.');
            }
        });
    }

    // ----- Smooth Scrolling -----
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

});

document.addEventListener("DOMContentLoaded", () => {
    const chatBox = document.getElementById("chatBox");
    const chatToggle = document.getElementById("chatToggle");
    const chatIcon = document.getElementById("chatIcon");
    const chatInput = document.getElementById("chatInput");
    const chatSend = document.getElementById("chatSend");
    const chatBody = document.getElementById("chatBody");

    if (!(chatBox && chatToggle && chatIcon && chatInput && chatSend && chatBody)) return;

    // Toggle Chat Box
    chatToggle.addEventListener("click", () => {
        chatBox.classList.toggle("collapsed");
        chatIcon.classList.toggle("fa-chevron-up");
        chatIcon.classList.toggle("fa-chevron-down");
    });

    // Append message
    const appendMessage = (msg, type = "bot") => {
        const div = document.createElement("div");
        div.classList.add(type === "user" ? "user-msg" : "bot-msg");
        div.textContent = msg;
        chatBody.appendChild(div);
        chatBody.scrollTop = chatBody.scrollHeight;
    };

    // Send Message
    const sendMessage = async () => {
        const message = chatInput.value.trim();
        if (!message) return;

        appendMessage(message, "user");
        chatInput.value = "";

        // Typing indicator
        const typingEl = document.createElement("div");
        typingEl.classList.add("bot-msg", "typing");
        typingEl.innerHTML = `<span></span><span></span><span></span>`;
        chatBody.appendChild(typingEl);
        chatBody.scrollTop = chatBody.scrollHeight;

        try {
            const response = await fetch("http://localhost:8080/api/chat/message", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json" // ensure we only accept JSON
                },
                body: JSON.stringify({ message })
            });

            // Remove typing indicator
            typingEl.remove();

            // Non-OK status
            if (!response.ok) {
                appendMessage(`Error: Server returned ${response.status}`, "bot");
                return;
            }

            // Safe JSON parsing
            let data;
            const text = await response.text();
            try {
                data = JSON.parse(text);
            } catch {
                appendMessage("Error: Server returned non-JSON response", "bot");
                console.error("Non-JSON response:", text);
                return;
            }

            if (data.type === "text") {
                appendMessage(data.response, "bot");
            } else if (data.type === "image") {
                const div = document.createElement("div");
                div.classList.add("bot-msg");
                div.innerHTML = `${data.prompt}<br><img src="${data.imageUrl}" alt="AI Image"/>`;
                chatBody.appendChild(div);
            }

            chatBody.scrollTop = chatBody.scrollHeight;
        } catch (err) {
            typingEl.remove();
            appendMessage(`Error: Please try again. (${err.message})`, "bot");
            console.error("Chat fetch error:", err);
        }
    };

    // Enter key
    chatInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") sendMessage();
    });

    chatSend.addEventListener("click", sendMessage);
});





