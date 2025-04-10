package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.entity.NoteEntity;
import com.example.quanlynhansu.models.request.note.NoteDisplayOfCaRequest;
import com.example.quanlynhansu.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/display_note_ca")
    public ResponseEntity<?> displayNoteOfCa(@RequestBody NoteDisplayOfCaRequest noteDisplayOfCaRequest){
        return noteService.displayNoteOfCa(noteDisplayOfCaRequest);
    }

}
