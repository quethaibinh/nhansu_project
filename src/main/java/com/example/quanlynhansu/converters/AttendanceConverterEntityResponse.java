package com.example.quanlynhansu.converters;

import com.example.quanlynhansu.models.entity.AttendanceEntity;
import com.example.quanlynhansu.models.response.AttendanceResponse;
import com.example.quanlynhansu.utils.PairClass;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class AttendanceConverterEntityResponse {

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm yyyy/MM/dd");

    public AttendanceResponse entityToResponse(List<AttendanceEntity> attendanceEntityList){

        AttendanceResponse attendanceResponse = new AttendanceResponse();
        Map<String, List<PairClass<String, String>>> map = new TreeMap<>();
        for(AttendanceEntity attendanceEntity: attendanceEntityList){
            StringBuilder time = new StringBuilder(sdf.format(attendanceEntity.getTimeStamp()));
            String day = time.substring(6); // lấy thông tin ngày để làm key cho map
            String hour = time.substring(0, 5); // lấy thông tin giờ của thời điểm chấm công
            if(map.containsKey(day)){
                List<PairClass<String, String>> list = map.get(day);
                list.add(new PairClass<>(hour, attendanceEntity.getStatus()));
                map.put(day, list);
            }
            else{
                List<PairClass<String, String>> list = new ArrayList<>();
                list.add(new PairClass<>(hour, attendanceEntity.getStatus()));
                map.put(day, list);
            }
        }
        attendanceResponse.setAttendanceOfDay(map);

        return attendanceResponse;
    }

}
