package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.NoteEntity;
import com.example.quanlynhansu.models.request.note.NoteDisplayOfCaRequest;
import com.example.quanlynhansu.models.request.note.NoteRequest;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.NoteRepo;
import com.example.quanlynhansu.services.NoteService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NoteSeriviceImpl implements NoteService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Override
    public ResponseEntity<?> createNote(NoteRequest noteRequest) {

        try{
            // lấy người dùng hiện tại
            EmployeeEntity employeeCurrent = employeeRepo.findOneByEmail(infoCurrentUserService.getCurrentUsername());

            NoteEntity noteEntity = modelMapper.map(noteRequest, NoteEntity.class);
            noteEntity.setEmployee(employeeCurrent);
            noteRepo.save(noteEntity);
            return ResponseEntity.ok("Successfull!");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> displayNoteOfCa(NoteDisplayOfCaRequest noteDisplayOfCaRequest) {

        try{
            // lấy thông tin người dùng hiện tại
            EmployeeEntity employeeCurrent = employeeRepo.findOneByEmail(infoCurrentUserService.getCurrentUsername());

            return ResponseEntity.ok(noteRepo.findByEmployeeAndWorkDateAndCa(employeeCurrent, noteDisplayOfCaRequest.getWorkDate(), noteDisplayOfCaRequest.getCa()));


        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }
}
