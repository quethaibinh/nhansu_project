package com.example.quanlynhansu.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "payroll")
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month")
    private String month;

    @Column(name = "payroll_month")
    private Long payrollMonth;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private EmployeeEntity employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getPayrollMonth() {
        return payrollMonth;
    }

    public void setPayrollMonth(Long payrollMonth) {
        this.payrollMonth = payrollMonth;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
