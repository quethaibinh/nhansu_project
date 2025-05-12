package com.example.quanlynhansu.services;

import ch.qos.logback.core.model.conditional.ElseModel;
import com.example.quanlynhansu.models.entity.CalculateScoreEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.PayrollEntity;
import com.example.quanlynhansu.repos.CalculateScoreRepo;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.PayrollRepo;
import com.example.quanlynhansu.repos.UserDetailsRepo;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.image.RescaleOp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private CalculateScoreRepo calculateScoreRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PayrollRepo payrollRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private EmailService emailService;

    public void saveCalculateScore(Long plusScore,
                                   Long minusScore,
                                   String reason,
                                   EmployeeEntity employee){
        CalculateScoreEntity calculateScoreEntity = new CalculateScoreEntity();
        calculateScoreEntity.setPlusScore(plusScore);
        calculateScoreEntity.setMinusScore(minusScore);
        calculateScoreEntity.setReason(reason);
        calculateScoreEntity.setEmployee(employee);
        calculateScoreRepo.save(calculateScoreEntity);
    }


    private void createScore() throws MessagingException {

        // lấy thời gian mùng 1 tháng hiện tại
        LocalDateTime endTime = LocalDate.now()
                .withDayOfMonth(1).atStartOfDay();
        // lấy thời gian mùng 1 tháng trước
        LocalDateTime startTime = LocalDate.now()
                .withDayOfMonth(1)            // chuyển về mùng 1 tháng hiện tại
                .minusMonths(1)               // trừ 1 tháng
                .atStartOfDay();              // đặt thời gian về 00:00

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // lấy hết user ra để tính điểm hàng tháng
        List<EmployeeEntity> employeeEntities = employeeRepo.findAll();
        for(EmployeeEntity user: employeeEntities){
            List<CalculateScoreEntity> calculateScoreEntities = calculateScoreRepo.findAllByEmployeeAndCreatedAtBetween(user, startTime, endTime);

            Long score = 0L;
            for(CalculateScoreEntity calculateScoreEntity: calculateScoreEntities){
                score += (calculateScoreEntity.getPlusScore() - calculateScoreEntity.getMinusScore()) * 1000;
            }

            PayrollEntity payrollEntity = new PayrollEntity();
            String month = LocalDate.now().withDayOfMonth(1).minusMonths(1).format(formatter);
            payrollEntity.setMonth(month);
            payrollEntity.setPayrollMonth(score);
            payrollEntity.setEmployee(user);

            Boolean exists = payrollRepo.existsByEmployeeAndMonth(user, month);
            if (!exists){
                payrollRepo.save(payrollEntity);
            }

            // HTML nội dung email
            String html = "<h2>Thông báo từ phòng nhân sự</h2>" +
                    "<p>Lương tháng trước của bạn đã tính xong.</p>" +
                    "<p>Lương của bạn + " + score + " của hệ hống thưởng phạt.</p>" +
                    "<p>Vui lòng truy cập web để xem chi tiết!</p>";

            emailService.sendHtmlEmailWithAttachment(
                    user.getEmail(),
                    "Thông báo tài khoản TYP",
                    html,
                    null
            );

        }

    }


    // Chạy vào 01:00 sáng ngày 5 hàng tháng
    @Scheduled(cron = "0 0 1 5 * ?")
    public void calculateScore() throws MessagingException {
        createScore();
    }


    public ResponseEntity<?> payrollOfLastMonth(){
        try{
            // lấy người dùng hiện tại
            EmployeeEntity employeeEntity = userDetailsRepo.findByUsername(infoCurrentUserService.getCurrentUsername()).getEmployee();

            LocalDate now = LocalDate.now();
            // Trừ đi 1 tháng để lấy tháng trước
            LocalDate lastMonth = now.minusMonths(1);

            // Định dạng: MM/yyyy (hoặc bạn có thể dùng "yyyy-MM" nếu muốn)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String monthString = lastMonth.format(formatter);

            PayrollEntity payrollEntity = payrollRepo.findByMonthAndEmployee(monthString, employeeEntity);

            return ResponseEntity.ok(payrollEntity);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    public ResponseEntity<?> historyCalculateScore(LocalDate startTime, LocalDate endTime){
        try{
            // lấy người dùng hiện tại
            EmployeeEntity employeeEntity = userDetailsRepo.findByUsername(infoCurrentUserService.getCurrentUsername()).getEmployee();

            List<CalculateScoreEntity> calculateScoreEntities = calculateScoreRepo.findAllByEmployeeAndCreatedAtBetween(employeeEntity, startTime.atStartOfDay(), endTime.atStartOfDay());
            return ResponseEntity.ok(calculateScoreEntities);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


}
