package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.DTO.ChatMessage;
import com.example.quanlynhansu.models.DTO.MessageDTO;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.MessageEntity;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.MessageRepo;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public MessageDTO saveMesage(ChatMessage message, Principal principal){
        MessageEntity messageEntity = new MessageEntity();

        try{
            EmployeeEntity sender = employeeRepo.findOneByEmail(principal.getName());
            EmployeeEntity receiver = employeeRepo.findOneByEmail(message.getReceiver());

            messageEntity.setSender(sender);
            messageEntity.setReceiver(receiver);
            messageEntity.setContent(message.getContent());
            sender.getSentMessages().add(messageEntity);
            receiver.getReceivedMessages().add(messageEntity);

            MessageDTO messageDTO = modelMapper.map(messageEntity, MessageDTO.class);
            messageDTO.setSenderEmail(messageEntity.getSender().getEmail());
            messageDTO.setReceiverEmail(messageEntity.getReceiver().getEmail());

            messageRepository.save(messageEntity);
            return messageDTO;

        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public ResponseEntity<?> displayChatHistory(String withEmail){

        try{
            // lấy email người dùng hiện tại đang đăng nhập
            String currentEmail = infoCurrentUserService.getCurrentUsername();

            // lấy lịch sử chat
            List<MessageEntity> messages = messageRepository.findChatHistoryBetween(withEmail, currentEmail);
            List<MessageDTO> messageDTOList = new ArrayList<>();
            for(MessageEntity message: messages){
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                messageDTO.setSenderEmail(message.getSender().getEmail());
                messageDTO.setReceiverEmail(message.getReceiver().getEmail());
                messageDTOList.add(messageDTO);
            }

            return ResponseEntity.ok(messageDTOList);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

}
