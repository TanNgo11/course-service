package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.response.CourseResponse;
import com.shadcn.courseservice.dto.response.LessonResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.entity.Course;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Lesson;
import com.shadcn.courseservice.entity.TeacherReference;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.repository.CourseRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.LessonRepository;
import com.shadcn.courseservice.repository.TeacherReferenceRepository;
import com.shadcn.courseservice.service.ILessonService;
import com.shadcn.courseservice.service.IReferenceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReferenceService implements IReferenceService {

    TeacherReferenceRepository teacherReferenceRepository;
    CourseRepository courseRepository;
    DepartmentRepository departmentRepository;
    @Override
    public void addATeacherReference(Long teacherId, Long courseId, Long departmentId) {
        Course course = getCourse(courseId);
        Department department = getDepartment(departmentId);

        TeacherReference teacherReference = new TeacherReference();
        teacherReference.setTeacherId(teacherId);
        teacherReference.setCourse(course);
        teacherReference.setDepartment(department);

        teacherReferenceRepository.save(teacherReference);
    }

    @Override
    public void deleteTeacherReferences(List<Long> referenceIds) {
        List<TeacherReference> teacherReferences = teacherReferenceRepository.findAllById(referenceIds);
        teacherReferenceRepository.deleteAll(teacherReferences);
    }

    Course getCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }
}
