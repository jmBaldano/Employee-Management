const params = new URLSearchParams(window.location.search);

if (params.get("unauthorized") === "true") {
    alert("Unauthorized access. Redirecting to homepage.");
    console.warn("Unauthorized access");
    // optional: clean the URL
    window.history.replaceState({}, document.title, "/home.html");
}