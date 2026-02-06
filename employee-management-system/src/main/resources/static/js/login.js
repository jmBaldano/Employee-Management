const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        console.log('Submitting login for', username);//for checking lang ng login

        fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        })
            .then(async res => {
                const txt = await res.text();
                console.log('Login response', res.status, txt);
                if (res.ok) {
                    window.location.href = "/admin.html";
                } else {
                    alert(txt || 'Login failed');
                }
            })
            .catch(err => {
                console.error('Login error', err);
                alert('An error occurred during login');
            });
    });
} else {
    console.warn('loginForm not found');
}