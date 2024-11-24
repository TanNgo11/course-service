package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.service.IReferenceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReferenceService implements IReferenceService {
    AcademicYearRepository academicYearRepository;
    TeacherReferenceRepository teacherReferenceRepository;
    CourseRepository courseRepository;
    DepartmentRepository departmentRepository;
    StudentReferenceRepository studentReferenceRepository;

    @Override
    @Transactional
    public void addATeacherReference(Long teacherId, Long courseId, Long departmentId) {
        Course course = getCourse(courseId);
        Department department = getDepartment(departmentId);

        TeacherReference teacherReference = TeacherReference.builder()
                .teacherId(teacherId)
                .course(course)
                .department(department)
                .build();

        teacherReferenceRepository.save(teacherReference);
    }

    @Override
    @Transactional
    public void deleteTeacherReferences(List<Long> referenceIds) {
        List<TeacherReference> teacherReferences = teacherReferenceRepository.findAllById(referenceIds);
        teacherReferenceRepository.deleteAll(teacherReferences);
    }

    @Override
    @Transactional
    public void addAStudentReference(Long studentId, Long academicYearId, Long departmentId, String name) {
        Department department = getDepartment(departmentId);
        AcademicYear academicYear = getAcademicYear(academicYearId);
        StudentReference studentReference = new StudentReference();
        studentReference.setStudentId(studentId);
        studentReference.setAcademicYear(academicYear);
        studentReference.setDepartment(department);
        studentReferenceRepository.save(studentReference);
    }

    @Override
    @Transactional
    public void deleteStudentReferences(List<Long> referenceIds) {
        List<StudentReference> studentReferences = studentReferenceRepository.findAllById(referenceIds);
        studentReferenceRepository.deleteAll(studentReferences);
    }

    Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    AcademicYear getAcademicYear(Long academicYearId) {
        return academicYearRepository
                .findById(academicYearId)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
    }
}
