package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.AttendanceConverterEntityResponse;
import com.example.quanlynhansu.models.entity.AttendanceEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.request.attendance.AttendanceRequest;
import com.example.quanlynhansu.models.response.AttendanceResponse;
import com.example.quanlynhansu.repos.AttendanceRepo;
import com.example.quanlynhansu.repos.CalculateScoreRepo;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.services.AttendanceService;
import com.example.quanlynhansu.services.PayrollService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import com.example.quanlynhansu.utils.CheckTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private AttendanceConverterEntityResponse attendanceConverterEntityResponse;

    @Autowired
    private CheckTime checkTime;

    @Autowired
    private PayrollService payrollService;

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm yyyy/MM/dd");

    public void addCheckInCheckOut(AttendanceRequest attendanceRequest) throws ParseException {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        EmployeeEntity employeeEntity = employeeRepo.findOneById(attendanceRequest.getEmployeeId());

        if (employeeEntity != null) {
            AttendanceEntity lastAttendance = attendanceRepo.findTopByEmployeeIdOrderByTimeStampDesc(attendanceRequest.getEmployeeId());

            String newDayAttendance = attendanceRequest.getTime();  // Thời gian mới gửi từ frontend
            Date newTime = sdf.parse(newDayAttendance);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newTime);

            boolean isNewDay = true;
            if (lastAttendance != null) {
                String oldDayAttendance = sdf.format(lastAttendance.getTimeStamp());
                isNewDay = checkTime.isAttendanceOfNewDay(oldDayAttendance.substring(6), newDayAttendance.substring(6));
            }

            // Xác định CHECKIN hoặc CHECKOUT
            if (lastAttendance == null || lastAttendance.getStatus().equals("CHECKOUT") || isNewDay) {
                attendanceEntity.setStatus("CHECKIN");

                if(lastAttendance != null && isNewDay){
                    // Tìm lần CHECKOUT gần nhất sau lần CHECKIN đó
                    AttendanceEntity nextCheckout = attendanceRepo
                            .findFirstByEmployeeIdAndTimeStampAfterAndStatusOrderByTimeStampAsc(
                                    employeeEntity.getId(), lastAttendance.getTimeStamp(), "CHECKOUT"
                            );

                    // Nếu không có CHECKOUT sau lần CHECKIN -> là quên checkout
                    if (nextCheckout == null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastAttendance.getTimeStamp());
                        cal.set(Calendar.HOUR_OF_DAY, 17);
                        cal.set(Calendar.MINUTE, 30);
                        cal.set(Calendar.SECOND, 0);
                        Date deadline = cal.getTime();

                        if (lastAttendance.getTimeStamp().before(deadline)) {
                            payrollService.saveCalculateScore(0L, 50L, "Không checkout sau 17h30 sau lần CHECKIN gần nhất", employeeEntity);
                        }
                    }
                }

                if(isNewDay){
                    // ⚠️ Kiểm tra đi muộn
                    Calendar workStart = Calendar.getInstance();
                    workStart.setTime(newTime);
                    workStart.set(Calendar.HOUR_OF_DAY, 8);
                    workStart.set(Calendar.MINUTE, 0);
                    workStart.set(Calendar.SECOND, 0);

                    if (newTime.after(workStart.getTime())) {
                        payrollService.saveCalculateScore(0L, 50L, "Đi làm muộn sau 8h sáng", employeeEntity);
                    }
                }

                if(!isNewDay && lastAttendance.getStatus().equals("CHECKOUT")){
                    // ⚠️ Kiểm tra ra ngoài quá 10 phút trong giờ làm
                    long diffMinutes = (newTime.getTime() - lastAttendance.getTimeStamp().getTime()) / (60 * 1000);
                    boolean inWorkTime = isInWorkingHours(lastAttendance.getTimeStamp());

                    if (inWorkTime && diffMinutes > 10) {
                        payrollService.saveCalculateScore(0L, 30L, "Ra ngoài quá 10 phút trong giờ làm việc", employeeEntity);
                    }
                }

            } else {
                attendanceEntity.setStatus("CHECKOUT");
            }

            attendanceEntity.setTimeStamp(newTime);
            attendanceEntity.setEmployee(employeeEntity);
            employeeEntity.getAttendance().add(attendanceEntity);
            attendanceRepo.save(attendanceEntity);
        }
    }

    // 🕒 Kiểm tra thời gian có nằm trong giờ làm việc không
    private boolean isInWorkingHours(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        double timeValue = hour + (minute / 60.0);

        return (timeValue >= 8.0 && timeValue <= 12.0) || (timeValue >= 13.5 && timeValue <= 17.5);
    }


    public AttendanceResponse displayAttendance(Long employeeId){ // employee = null => currentUser, != null => admin, Manager

        if(employeeId == null){ // current user
            String username = infoCurrentUserService.getCurrentUsername();
            employeeId = employeeRepo.findOneByEmail(username).getId();
        }
        List<AttendanceEntity> attendanceEntityList = attendanceRepo.findAllByEmployee_Id(employeeId);
        return attendanceConverterEntityResponse.entityToResponse(attendanceEntityList);

//        return null;
    }

}
