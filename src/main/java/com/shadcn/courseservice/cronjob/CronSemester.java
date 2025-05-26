package com.shadcn.courseservice.cronjob;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.repository.SemesterRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CronSemester {
    SemesterRepository semesterRepository;

//    @Bean
//    @Scheduled(cron = "0 0 0 * * *")
//    public void updateSemesterStatus() {
//        log.info("Update semester status");
//
//        List<Semester> semesters = semesterRepository.findAll();
//        LocalDate now = LocalDate.now();
//        for (Semester semester : semesters) {
//            semester.setSemesterActive(now.isAfter(semester.getStartDate()) && now.isBefore(semester.getEndDate()));
//        }
//
//        semesterRepository.saveAll(semesters);
//    }

    @Bean
    @Scheduled(cron = "0 0 0 * * 1")
    public void fetchStudentProfile() {
        log.info("Fetch student profile");
    }
}
