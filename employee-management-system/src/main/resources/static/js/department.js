const API_URL = "http://localhost:8080/api/departments";

document.addEventListener("DOMContentLoaded", loadDepartments);

// READ (all)
function loadDepartments() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("deptTableBody");
            tbody.innerHTML = "";

            data.forEach(dept => {
                tbody.innerHTML += `
                    <tr>
                        <td>${dept.id}</td>
                        <td>${dept.department}</td>
                        <td>
                            <button onclick="editDepartment(${dept.id}, '${dept.department}')">Edit</button>
                            <button onclick="deleteDepartment(${dept.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(err => alert("Failed to load departments"));
}

// CREATE + UPDATE
function saveDepartment() {
    const id = document.getElementById("deptId").value;
    const department = document.getElementById("deptName").value.trim();

    if (!department) {
        alert("Department name is required");
        return;
    }

    const method = id ? "PUT" : "POST";
    const url = id ? `${API_URL}/${id}` : API_URL;

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ department })
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Operation failed");
            }
            return res.json();
        })
        .then(() => {
            resetForm();
            loadDepartments();
        })
        .catch(err => alert(err.message));
}

// EDIT (populate form)
function editDepartment(id, department) {
    document.getElementById("deptId").value = id;
    document.getElementById("deptName").value = department;
}

// DELETE
function deleteDepartment(id) {
    if (!confirm("Are you sure you want to delete this department?")) return;

    fetch(`${API_URL}/${id}`, {
        method: "DELETE"
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Delete failed");
            }
            loadDepartments();
        })
        .catch(err => alert(err.message));
}

// Reset form
function resetForm() {
    document.getElementById("deptId").value = "";
    document.getElementById("deptName").value = "";
}
