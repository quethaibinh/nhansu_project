package com.example.quanlynhansu.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "calculate_score")
public class CalculateScoreEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plus_score")
    private Long plusScore;

    @Column(name = "minus_score")
    private Long minusScore;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private EmployeeEntity employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlusScore() {
        return plusScore;
    }

    public void setPlusScore(Long plusScore) {
        this.plusScore = plusScore;
    }

    public Long getMinusScore() {
        return minusScore;
    }

    public void setMinusScore(Long minusScore) {
        this.minusScore = minusScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
