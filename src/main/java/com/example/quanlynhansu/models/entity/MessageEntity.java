package com.example.quanlynhansu.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "sender_id")
    @JsonBackReference
    private EmployeeEntity sender;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "receiver_id")
    @JsonBackReference
    private EmployeeEntity receiver;

    @Column(name = "content")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeEntity getSender() {
        return sender;
    }

    public void setSender(EmployeeEntity sender) {
        this.sender = sender;
    }

    public EmployeeEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(EmployeeEntity receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
