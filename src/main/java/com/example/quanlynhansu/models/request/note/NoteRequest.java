package com.example.quanlynhansu.models.request.note;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class NoteRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private String ca;
    private String note;

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
