package com.shadcn.courseservice.service;

import com.shadcn.courseservice.entity.Course;

public interface IScheduleService {
    void generateTimeTable(Course course);
}
