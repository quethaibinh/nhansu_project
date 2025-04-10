package com.example.quanlynhansu.models.request.note;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class NoteDisplayOfCaRequest {

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate workDate;

    private String ca;

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
}
