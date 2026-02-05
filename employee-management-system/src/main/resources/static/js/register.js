const form = document.getElementById('registerForm');
console.log('register.js loaded');
if (form) {
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // prevent page reload

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // console.log('Submitting register for', username);

        fetch('/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password })
        })
            .then(async res => {
                const txt = await res.text();
                console.log('Register response status', res.status, txt);
                alert(txt);
                if (res.ok) {
                    window.location.href = "/login.html";
                }
            })
            .catch(err => {
                console.error('Register error', err);
                alert('Network error during registration');
            });
    });
} else {
    console.warn('registerForm not found');
}