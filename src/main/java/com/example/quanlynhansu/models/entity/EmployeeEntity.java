package com.example.quanlynhansu.models.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "employees")
public class EmployeeEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "address")
    private String address;

    @Column(name = "hire_date")
    private Date hireDate;

    @Column(name = "status")
    private String status;

    @Column(name = "avatar_url", columnDefinition = "VARCHAR(255) DEFAULT 'http://localhost:9000/btlweb/avatar_defaultPng.pnghttp://localhost:9000/btlweb/avatar_defaultPng.png'")
    private String avatarUrl;

    @Column(name = "position")
    private String position;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Chỉ serialize danh sách này
    private List<AccountEntity> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference // Chỉ serialize danh sách này
    private List<ContractEntity> contracts;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WorkHistoryEntity> workHistory;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AttendanceEntity> attendance;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WorkScheduleEntity> workSchedule;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<NoteEntity> note;

    public List<WorkScheduleEntity> getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(List<WorkScheduleEntity> workSchedule) {
        this.workSchedule = workSchedule;
    }

    public List<AttendanceEntity> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<AttendanceEntity> attendance) {
        this.attendance = attendance;
    }

    public List<WorkHistoryEntity> getWorkHistory() {
        return workHistory;
    }

    public void setWorkHistory(List<WorkHistoryEntity> workHistory) {
        this.workHistory = workHistory;
    }

    public List<ContractEntity> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractEntity> contracts) {
        this.contracts = contracts;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
