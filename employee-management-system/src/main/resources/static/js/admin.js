import { urls } from "./url.js";

let currentEditId = null;
let allEmployees = [];
let allDepartments = [];
let currentPage = 0;
let pageSize = 5;
let totalPages = 0;


// Check if user is logged in on page load
window.addEventListener('load', () => {
    checkAuth();
    loadDepartments();
    loadEmployees();
});

function checkAuth() {
    // Simple check: if user tries to access admin page without login, redirect
    // This is a basic client-side check; real security is server-side
    fetch(urls.employees.base, { method: 'GET' })
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
    fetch(urls.employees.departmentsAll)
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

function loadEmployees(page = 0) {
    currentPage = page;

    const searchName = document.getElementById('searchName').value.trim();
    const searchID = document.getElementById('searchId').value.trim();
    const filterDeptId = document.getElementById('filterDept').value;
    const deptIdParam = filterDeptId !== "" ? Number(filterDeptId) : null;
    const searchAge = document.getElementById('searchAge').value;

    let url = `${urls.employees.search}?page=${currentPage}&size=${pageSize}`; // pagination params

    if (searchID) {
        url += `&employeeId=${encodeURIComponent(searchID)}`;
    } else if (searchName) {
        url += `&name=${encodeURIComponent(searchName)}`;
    } else if (deptIdParam !== null) {
        url += `&departmentId=${deptIdParam}`;
    } else if (searchAge) {
        url += `&age=${encodeURIComponent(searchAge)}`;
    }

    fetch(url)
        .then(res => {
            if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
            return res.json();
        })
        .then(data => {
            // Expecting Spring Boot Page<EmployeeModel> object
            allEmployees = data.content;
            totalPages = data.totalPages;
            renderTable(allEmployees);
            updatePaginationControls();
        })
        .catch(err => {
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
            <td>₱${emp.salary.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",")}</td>
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

    fetch(urls.employees.byId(currentEditId), {
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

    fetch(urls.employees.byId(id), { method: 'DELETE' })
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
    loadEmployees(0);
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

    //Validation for age submission
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

    fetch(urls.employees.base, {
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

const today = new Date();

// Calculate the latest allowed birth date for 18 years old
const year = today.getFullYear() - 18;
const month = String(today.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
const day = String(today.getDate()).padStart(2, '0');

const maxDate = `${year}-${month}-${day}`;



// Set the max attribute on the date input
const birthInputs = ['birthDate', 'editBirthDate'];
birthInputs.forEach(id => {
    const input = document.getElementById(id);
    if (input) {
        input.setAttribute('max', maxDate);
    }
});




//pagination controls
function nextPage() {
    if (currentPage + 1 < totalPages) {
        loadEmployees(currentPage + 1);
    }
}

function prevPage() {
    if (currentPage > 0) {
        loadEmployees(currentPage - 1);
    }
}

function updatePaginationControls() {
    const pageInfo = document.getElementById('pageInfo');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (!pageInfo || !prevBtn || !nextBtn) return;

    pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;
    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage + 1 >= totalPages;
}

//to fix onlick methods in html, since the type of this js is module
window.loadEmployees = loadEmployees;
window.nextPage = nextPage;
window.prevPage = prevPage;
window.resetFilters = resetFilters;

