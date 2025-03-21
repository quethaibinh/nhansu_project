package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.AttendanceConverterEntityResponse;
import com.example.quanlynhansu.models.entity.AttendanceEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.request.attendance.AttendanceRequest;
import com.example.quanlynhansu.models.response.AttendanceResponse;
import com.example.quanlynhansu.repos.AttendanceRepo;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.services.AttendanceService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import com.example.quanlynhansu.utils.CheckTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm yyyy/MM/dd");

    public void addCheckInCheckOut(AttendanceRequest attendanceRequest) throws ParseException {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        EmployeeEntity employeeEntity = employeeRepo.findOneById(attendanceRequest.getEmployeeId());

        if (employeeEntity != null) {
            AttendanceEntity lastAttendance = attendanceRepo.findTopByEmployeeIdOrderByTimeStampDesc(attendanceRequest.getEmployeeId());

            // Nếu là lần đầu chấm công, mặc định là CHECKIN
            if (lastAttendance == null) {
                attendanceEntity.setTimeStamp(sdf.parse(attendanceRequest.getTime()));
                attendanceEntity.setStatus("CHECKIN");
            } else {
                String oldDayAttendance = sdf.format(lastAttendance.getTimeStamp());  // Ngày của lần chấm công gần nhất
                String newDayAttendance = attendanceRequest.getTime();  // Ngày mới

                boolean isNewDay = checkTime.isAttendanceOfNewDay(oldDayAttendance.substring(6), newDayAttendance.substring(6));

                if (lastAttendance.getStatus().equals("CHECKOUT") || isNewDay) {
                    attendanceEntity.setStatus("CHECKIN"); // Nếu là ngày mới hoặc đã CHECKOUT trước đó -> CHECKIN
                } else {
                    attendanceEntity.setStatus("CHECKOUT"); // Nếu chưa CHECKOUT -> CHECKOUT
                }

                attendanceEntity.setTimeStamp(sdf.parse(newDayAttendance));
            }

            // Gán nhân viên vào chấm công
            attendanceEntity.setEmployee(employeeEntity);

            // Thêm vào danh sách chấm công của nhân viên
            employeeEntity.getAttendance().add(attendanceEntity);

            // Lưu vào DB
            attendanceRepo.save(attendanceEntity);
        }
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
