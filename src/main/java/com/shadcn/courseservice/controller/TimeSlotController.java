package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_TIME_SLOTS;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadcn.courseservice.dto.request.timeslot.DeleteTimeslotRequest;
import com.shadcn.courseservice.dto.response.ApiResponse;
import com.shadcn.courseservice.dto.response.timeslot.TimeslotResponse;
import com.shadcn.courseservice.service.ITimeSlotService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_TIME_SLOTS)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class TimeSlotController {
    ITimeSlotService timeSlotService;

    @PostMapping("/semesters/{semesterId}")
    public ApiResponse<Void> initialTimeSlotForSemester(@PathVariable Long semesterId) {
        timeSlotService.initializeTimeSlotsForSemester(semesterId);
        return ApiResponse.empty();
    }

    @DeleteMapping
    public ApiResponse<Void> removeTimeslotByTeacherIdAndTimeSlotId(@RequestBody DeleteTimeslotRequest request) {
        timeSlotService.removeTimeslotByTeacherIdAndTimeSlotId(request.getTeacherId(), request.getTimeSlotId());
        return ApiResponse.empty();
    }

    @GetMapping("/teachers/{teacherId}/semesters/{semesterId}")
    public ApiResponse<List<TimeslotResponse>> getTimeSlotsByTeacherIdAndSemesterId(
            @PathVariable Long teacherId, @PathVariable Long semesterId) {
        return ApiResponse.success(timeSlotService.getTimeSlotsByTeacherIdAndSemesterId(teacherId, semesterId));
    }
}
