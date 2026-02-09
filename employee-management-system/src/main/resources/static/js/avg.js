function formatSalary(amount) {
    return `₱ ${amount.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
}

fetch('/api/employees/averageSalary')
    .then(res => res.json())
    .then(avgSalary => {
        document.getElementById('avgSalary').innerText = formatSalary(avgSalary);
    });

    fetch('/api/employees/averageAge')
        .then(res => res.json())
        .then(avgAge => {
            const el = document.getElementById('avgAge');
            if (el) {
                el.innerText = `${Math.round(avgAge)} years`;
            }
        })
        .catch(err => console.error('Error fetching average age:', err));


