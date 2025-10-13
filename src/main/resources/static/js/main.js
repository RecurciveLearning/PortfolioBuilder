document.addEventListener('DOMContentLoaded', function() {
    AOS.init({
        duration: 1000,
        once: true
    });

    const typedElement = document.getElementById('typed-text');
    if (typedElement) {
        new Typed('#typed-text', {
            strings: ['Java Backend Developer', 'Spring Boot Expert', 'Microservices Architect', 'Problem Solver'],
            typeSpeed: 50,
            backSpeed: 30,
            loop: true
        });
    }

    const themeToggle = document.getElementById('themeToggle');
    if (themeToggle) {
        themeToggle.addEventListener('click', function() {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            this.querySelector('i').className = newTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
        });

        const savedTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', savedTheme);
        themeToggle.querySelector('i').className = savedTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    }

    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', async function(e) {
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
                }
            } catch (error) {
                alert('Error sending message. Please try again.');
            }
        });
    }

    const meetingForm = document.getElementById('meetingForm');
    if (meetingForm) {
        meetingForm.addEventListener('submit', async function(e) {
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
                }
            } catch (error) {
                alert('Error submitting meeting request. Please try again.');
            }
        });
    }

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

document.addEventListener('DOMContentLoaded', function () {
    const chatBox = document.getElementById("chatBox");
    const chatToggle = document.getElementById("chatToggle");
    const chatIcon = document.getElementById("chatIcon");
    const chatInput = document.getElementById("chatInput");
    const chatSend = document.getElementById("chatSend");
    const chatBody = document.getElementById("chatBody");

    // Toggle Chat Box
    chatToggle.addEventListener("click", () => {
        chatBox.classList.toggle("collapsed");
        chatIcon.classList.toggle("fa-chevron-up");
        chatIcon.classList.toggle("fa-chevron-down");
    });

    // Send Message
    const sendMessage = async () => {
        const message = chatInput.value.trim();
        if (!message) return;

        // Show user message
        chatBody.innerHTML += `<div class="user-msg">${message}</div>`;
        chatBody.scrollTop = chatBody.scrollHeight;
        chatInput.value = "";

        // Show typing indicator
        const typingEl = document.createElement("div");
        typingEl.classList.add("bot-msg", "typing");
        typingEl.innerHTML = `<span></span><span></span><span></span>`;
        chatBody.appendChild(typingEl);
        chatBody.scrollTop = chatBody.scrollHeight;

        try {
            const response = await fetch("/api/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message })
            });

            const data = await response.json();
            typingEl.remove();

            if (data.type === "text") {
                chatBody.innerHTML += `<div class="bot-msg">${data.response}</div>`;
            } else if (data.type === "image") {
                chatBody.innerHTML += `<div class="bot-msg">${data.prompt}<br><img src="${data.imageUrl}" alt="AI Image"/></div>`;
            }

            chatBody.scrollTop = chatBody.scrollHeight;
        } catch (err) {
            typingEl.remove();
            chatBody.innerHTML += `<div class="bot-msg">Error: Please try again.</div>`;
        }
    };

    chatInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") sendMessage();
    });

    chatSend.addEventListener("click", sendMessage);
});


