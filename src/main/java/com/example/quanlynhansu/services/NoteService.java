package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.entity.NoteEntity;
import com.example.quanlynhansu.models.request.note.NoteDisplayOfCaRequest;
import com.example.quanlynhansu.models.request.note.NoteRequest;
import org.springframework.http.ResponseEntity;

public interface NoteService {

    ResponseEntity<?> createNote(NoteRequest noteRequest);
    ResponseEntity<?> displayNoteOfCa(NoteDisplayOfCaRequest noteDisplayOfCaRequest);

}
