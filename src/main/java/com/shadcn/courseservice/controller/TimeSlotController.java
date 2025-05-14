package com.shadcn.courseservice.controller;

import static com.shadcn.courseservice.constant.PathConstant.API_V1_TIME_SLOTS;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadcn.courseservice.dto.response.ApiResponse;
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
}
