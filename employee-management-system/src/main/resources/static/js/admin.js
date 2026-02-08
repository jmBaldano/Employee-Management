console.log('admin.js loaded');

let currentEditId = null;
let allEmployees = [];
let allDepartments = [];

// Check if user is logged in on page load
window.addEventListener('load', () => {
    checkAuth();
    loadDepartments();
    loadEmployees();
});

function checkAuth() {
    // Simple check: if user tries to access admin page without login, redirect
    // This is a basic client-side check; real security is server-side
    fetch('/api/employees', { method: 'GET' })
        .then(res => {
            if (res.status === 401 || res.status === 403) {
                alert('Please log in first');
                window.location.href = '/login.html';
            }
        })
        .catch(err => {
            console.error('Auth check failed:', err);
            window.location.href = '/login.html';
        });
}

function logout() {
    // Simple logout: clear and redirect
    alert('Logged out');
    window.location.href = '/login.html';
}

function loadDepartments() {
    fetch('/api/employees/departments/all')
        .then(res => res.json())
        .then(depts => {
            allDepartments = depts;
            populateDepartmentSelects(depts);
        })
        .catch(err => console.error('Load departments error:', err));
}

function populateDepartmentSelects(depts) {
    const selects = ['department', 'filterDept', 'editDepartment'];
    selects.forEach(id => {
        const select = document.getElementById(id);
        if (!select) return;
        const currentVal = select.value;
        select.innerHTML = '';
        if (id !== 'filterDept') {
            select.innerHTML = '<option value="">Select Department</option>';
        } else {
            select.innerHTML = '<option value="">All Departments</option>';
        }
        depts.forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.id;
            option.textContent = dept.department;
            select.appendChild(option);
        });
        if (currentVal) select.value = currentVal;
    });
}

function loadEmployees() {
    const searchName = document.getElementById('searchName').value.trim();
    const searchID = document.getElementById('searchId').value.trim();
    const filterDeptId = document.getElementById('filterDept').value;

    let url = '/api/employees'; // get all employees if no input value

    //look for user input
    if (searchID) {
        url = `/api/employees/search?employeeId=${encodeURIComponent(searchID)}`;
    } else if (searchName) {
        url = `/api/employees/search?name=${encodeURIComponent(searchName)}`;
    } else if (filterDeptId !== "") {
        url = `/api/employees/department/${filterDeptId}`;
    }
    //fetch the url and converts to JSON
    fetch(url)
        .then(res => {
            if (!res.ok) { // check for HTTP errors
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json(); // only parse JSON if status is OK
        })
        .then(employees => {
            allEmployees = employees;
            renderTable(employees);
        })
        .catch(err => {
            console.error('Load employees error:', err);
            alert('Failed to load employees');
        });
}

function renderTable(employees) {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';

    if (employees.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No employees found</td></tr>';
        return;
    }

    employees.forEach(emp => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${emp.employeeId}</td>
            <td>${emp.name}</td>
            <td>${emp.age}</td>
            <td>$${emp.salary.toFixed(2)}</td>
            <td>${emp.department ? emp.department.department : '-'}</td>
            <td>
                <div class="action-btns">
                    <button class="btn btn-primary" onclick="editEmployee(${emp.id})">Edit</button>
                    <button class="btn btn-danger" onclick="deleteEmployee(${emp.id})">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function editEmployee(id) {
    const emp = allEmployees.find(e => e.id === id);
    if (!emp) return;

    currentEditId = id;
    document.getElementById('editName').value = emp.name;
    document.getElementById('editSalary').value = emp.salary;
    document.getElementById('editBirthDate').value = emp.birthDate;
    document.getElementById('editDepartment').value = emp.department ? emp.department.id : '';

    document.getElementById('editModal').classList.add('active');
}

function closeEditModal() {
    document.getElementById('editModal').classList.remove('active');
    currentEditId = null;
}

document.getElementById('editForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const updatedEmp = {
        name: document.getElementById('editName').value,
        salary: parseFloat(document.getElementById('editSalary').value),
        birthDate: document.getElementById('editBirthDate').value,
        department: allDepartments.find(d => d.id == document.getElementById('editDepartment').value) || null
    };

    fetch(`/api/employees/${currentEditId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedEmp)
    })
        .then(res => {
            if (res.ok) {
                alert('Employee updated successfully');
                closeEditModal();
                loadEmployees();
            } else {
                alert('Failed to update employee');
            }
        })
        .catch(err => {
            console.error('Update error:', err);
            alert('Error updating employee');
        });
});

function deleteEmployee(id) {
    if (!confirm('Are you sure you want to delete this employee?')) return;

    fetch(`/api/employees/${id}`, { method: 'DELETE' })
        .then(res => {
            if (res.ok) {
                alert('Employee deleted successfully');
                loadEmployees();
            } else {
                alert('Failed to delete employee');
            }
        })
        .catch(err => {
            console.error('Delete error:', err);
            alert('Error deleting employee');
        });
}

function resetForm() {
    document.getElementById('employeeForm').reset();
    document.getElementById('formTitle').textContent = 'Add Employee';
}

function resetFilters() {
    document.getElementById('searchName').value = '';
    document.getElementById('searchId').value ='';
    document.getElementById('filterDept').value = '';
    loadEmployees();
}

// calculate age from birth date then validate if the age is at least 18
function getAge(birthDate) {
    const today = new Date();
    const birth = new Date(birthDate);

    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();

    if (
        monthDiff < 0 ||
        (monthDiff === 0 && today.getDate() < birth.getDate())
    ) {
        age--;
    }

    return age;
}




document.getElementById('employeeForm').addEventListener('submit', function (e) {
    e.preventDefault();

    //Vakidation for age submission
    const birthDateValue = document.getElementById('birthDate').value;

    const age = getAge(birthDateValue);

    if (age < 18) {
    alert("Employee must be at least 18 years old.");
    // resetForm();
    return;
}

    const newEmp = {
        name: document.getElementById('name').value,
        salary: parseFloat(document.getElementById('salary').value),
        birthDate: birthDateValue,
        department: allDepartments.find(d => d.id == document.getElementById('department').value) || null
    };

    fetch('/api/employees', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newEmp)
    })
        .then(res => {
            if (res.ok) {
                alert('Employee created successfully');
                resetForm();
                loadEmployees();
            } else {
                alert('Failed to create employee');
            }
        })
        .catch(err => {
            console.error('Create error:', err);
            alert('Error creating employee');
        });
});

