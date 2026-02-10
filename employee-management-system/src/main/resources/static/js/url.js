export const urls = {
    employees: {
        base: "/api/employees",
        all: "/api/employees",
        byId: (id) => `/api/employees/${id}`,
        search: "/api/employees/search",
        averageSalary: "/api/employees/averageSalary",
        averageAge: "/api/employees/averageAge",
        departmentsAll: "/api/employees/departments/all"
    }
};
