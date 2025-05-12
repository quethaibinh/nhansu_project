package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.WorkScheduleEntity;
import com.example.quanlynhansu.models.request.WorkSchedule.WorkScheduleRequest;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.WorkScheduleRepo;
import com.example.quanlynhansu.services.WorkScheduleService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class WorkScheduleServiceImpl implements WorkScheduleService {

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private WorkScheduleRepo workScheduleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    // người chọn làm lịch sáng làm 08:00 -> 12:00, chiều từ 01:30 -> 17:30
    // nếu người chọn cả sáng và chiều => lịch từ 08:00 -> 17:30, kiểm tra bằng cách startTime < 12:00 && endTime > 12:00, lương sẽ tự động trừ tiếng rưỡi
    public ResponseEntity<?> createAndUpdateWorkSchedule(List<WorkScheduleRequest> workScheduleRequests) {

        // kiểm tra số tiếng làm được đăng ký
        long time = 0;
        for(WorkScheduleRequest workScheduleRequest: workScheduleRequests){
            if(workScheduleRequest.getStartTime().equals(LocalTime.of(8, 0)) && workScheduleRequest.getEndTime().equals(LocalTime.of(17, 30))) {
                time += 8;
            } else time += 4;
        }
        if(time < 20){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("số buổi làm tối thiểu trong 1 tuần là 5 buổi (20 tiếng)");
        }

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.MONDAY) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("chỉ được đăng ký vào chủ nhật và thứ 2 hàng tuần!");
        }
        boolean isLate = dayOfWeek == DayOfWeek.MONDAY; // nếu chủ nhật không đăng ký thì bị muộn và chuyển sang thứ 2

        // lấy người dùng từ tài khoản đăng nhập hiện tại
        try{
            String username = infoCurrentUserService.getCurrentUsername();
            EmployeeEntity employeeCurrent = employeeRepo.findOneByEmail(username);
            if(!employeeCurrent.getPosition().equals("Part_time") && !employeeCurrent.getPosition().equals("Internship")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Lịch của bạn đã được hệ thống tự động tạo!");
            }

            // Lấy tuần hiện tại từ 1 ngày bất kỳ trong request (ví dụ phần tử đầu tiên)
            LocalDate anyDate = workScheduleRequests.get(0).getWorkDate();
            LocalDate mondayOfNextWeek = anyDate.with(DayOfWeek.MONDAY);
            LocalDate sundayOfNextWeek = anyDate.with(DayOfWeek.SUNDAY);

            // kiểm tra người dùng hiện tại đã đăng ký lịch chưa, nếu chưa mới được đăng ký, không thì là update
            List<WorkScheduleEntity> existingSchedules = workScheduleRepo
                    .findByEmployeeAndWorkDateBetween(employeeCurrent, mondayOfNextWeek, sundayOfNextWeek);

            if (!existingSchedules.isEmpty()) { // fix cái này thành update, chỉ cần xóa exitingSchedules và thêm mới.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bạn đã đăng ký lịch làm việc cho tuần này rồi, không thể đăng ký lại.");
            }

            // chuyển đổi dữ liệu từ request sang entity
            for(WorkScheduleRequest workScheduleRequest : workScheduleRequests){
                WorkScheduleEntity workScheduleEntity = modelMapper.map(workScheduleRequest, WorkScheduleEntity.class);
                workScheduleEntity.setEmployee(employeeCurrent);
                workScheduleEntity.setLateSunday(isLate);
                workScheduleEntity.setLateMonday(false);
                employeeCurrent.getWorkSchedule().add(workScheduleEntity); // thêm dữ liệu mới vào mà không mất dữ liệu cũ, nếu dùng set thì dữ liệu cũ sẽ bị xóa.

                workScheduleRepo.save(workScheduleEntity);
                System.out.println("success!");
            }
            return ResponseEntity.ok("successful!");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> displayScheduleOfCurrentWeek() {
        try{

            // lấy thứ 2 và chủ nhật của tuần hiện tại
            LocalDate today = LocalDate.now();
            LocalDate mondayOfNextWeek = today.with(DayOfWeek.MONDAY);
            LocalDate sundayOfNextWeek = today.with(DayOfWeek.SUNDAY);

            // lấy thông tin người đang đăng nhập hiện tại
            EmployeeEntity employeeCurrent = employeeRepo.findOneByEmail(infoCurrentUserService.getCurrentUsername());

            return ResponseEntity.ok(workScheduleRepo.findByEmployeeAndWorkDateBetween(employeeCurrent, mondayOfNextWeek, sundayOfNextWeek));

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> displayScheduleOfMonth(String monthYear) {

        try{
            // lấy ra người dùng đang đăng nhập
            EmployeeEntity employeeCurrent = employeeRepo.findOneByEmail(infoCurrentUserService.getCurrentUsername());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

            // Parse chuỗi thành LocalDate (lấy ngày đầu tiên của tháng)
            LocalDate firstDay = LocalDate.parse("01-" + monthYear, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate firstDayOfMonth = firstDay.with(TemporalAdjusters.firstDayOfMonth()); // ngày đầu tháng
            LocalDate lastDayOfMonth = firstDay.with(TemporalAdjusters.lastDayOfMonth()); // ngày cuối tháng

            return ResponseEntity.ok(workScheduleRepo.findByEmployeeAndWorkDateBetween(employeeCurrent, firstDayOfMonth, lastDayOfMonth));

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> displayDayAndMonthOfCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate tuesday = today.with(DayOfWeek.TUESDAY);
        LocalDate wenesday = today.with(DayOfWeek.WEDNESDAY);
        LocalDate thursday = today.with(DayOfWeek.THURSDAY);
        LocalDate friday = today.with(DayOfWeek.FRIDAY);
        LocalDate saturday = today.with(DayOfWeek.SATURDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);
        return ResponseEntity.ok(List.of(monday, tuesday, wenesday, thursday, friday, saturday, sunday));
    }


    // Tự động chạy vào 23:59 thứ Bảy hàng tuần để tự đồng tạo lịch làm việc full tuần cho nhân viên fulltime
    @Scheduled(cron = "0 59 23 ? * SAT")
    public void generateDefaultSchedulesForFullTimeEmployees() {
        LocalDate now = LocalDate.now();
        LocalDate nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        // Lấy danh sách nhân viên không phải internship hoặc parttime
        List<EmployeeEntity> fullTimeEmployees = employeeRepo.findByPositionNotIn(List.of("Internship", "Part_time"));

        for (EmployeeEntity emp : fullTimeEmployees) {
            // Tạo lịch từ thứ 2 đến thứ 6 (tuần tới)

            for (int i = 0; i < 5; i++) {
                LocalDate workDate = now.plusDays(i); // Tạo ra một ngày làm việc cụ thể bằng cách cộng thêm i ngày vào ngày thứ Hai (nextMonday).

                WorkScheduleEntity schedule = new WorkScheduleEntity();
                schedule.setEmployee(emp);
                schedule.setWorkDate(workDate);
                schedule.setStartTime(LocalTime.of(8, 0));
                schedule.setEndTime(LocalTime.of(17, 30));
                schedule.setLateSunday(false);
                schedule.setStatus("approved"); // vì là hệ thống tạo
                schedule.setNote("");
                emp.getWorkSchedule().add(schedule); // thêm dữ liệu mới vào mà không mất dữ liệu cũ, nếu dùng set thì dữ liệu cũ sẽ bị xóa.

                workScheduleRepo.save(schedule);

            }
        }

        System.out.println("Tạo lịch mặc định cho full-time employees thành công.");
    }

}
