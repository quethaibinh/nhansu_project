package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.entity.NoteEntity;
import com.example.quanlynhansu.models.request.note.NoteRequest;
import com.example.quanlynhansu.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/note")
public class NoteApi {

    @Autowired
    private NoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NoteRequest noteRequest){
        return noteService.createNote(noteRequest);
    }

}
