package com.shadcn.courseservice.service.impl;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.request.teacher.UpdateTeacherReferenceRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.user.UserProfileResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.TeacherMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.repository.httpClient.IdentityClient;
import com.shadcn.courseservice.service.IReferenceService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

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
    TeacherMapper teacherMapper;
    SemesterRepository semesterRepository;
    IdentityClient identityClient;

    @Override
    @Transactional
    public void addATeacherReference(Long teacherId, Long departmentId) {
        Department department = getDepartment(departmentId);

        TeacherReference teacherReference = TeacherReference.builder()
                .teacherId(teacherId)
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
    public PageResponse<UserProfileResponse> getAvailableTeachersInCoursesBySemesterId(
            Long semesterId, int current, int pageSize) {
        Semester semester = semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        List<Course> courses = courseRepository.findAllBySemester(semester);

        List<TeacherReference> teacherReferences = teacherReferenceRepository.findAllByCourseIds(
                courses.stream().map(Course::getId).toList());

        List<Long> teacherIds = teacherReferences.stream()
                .map(TeacherReference::getTeacherId)
                .distinct()
                .toList();

        List<UserProfileResponse> teacherProfiles =
                identityClient.getUserProfileResponses(teacherIds).getResult();

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<UserProfileResponse> responses = new PageImpl<>(teacherProfiles, pageable, teacherProfiles.size());

        return ConverToPaginationResponse.toPageResponse(responses, Function.identity(), current);
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

    @Override
    @Transactional
    public void updateTeacherReference(UpdateTeacherReferenceRequest updateTeacherReferenceRequest) {
        TeacherReference teacherReference = teacherReferenceRepository
                .findByTeacherId(updateTeacherReferenceRequest.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        teacherMapper.updateTeacherReference(teacherReference, updateTeacherReferenceRequest);

        teacherReferenceRepository.save(teacherReference);
    }
}
