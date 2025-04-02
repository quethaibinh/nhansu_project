package com.example.quanlynhansu.models.request.contract;

public class ContractRequest {

    private Long id;
    private Long employeeId;
    private String contractType; //'Full_time', 'Part_time', 'Internship'
    private String department; // 'Ke_toan', 'Nhan_su', 'IT', 'Sale', 'Kinh_doanh', 'Thiet_ke'
    private String startDate;
    private String endDate;
    private Double salary;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

}
