package com.shadcn.courseservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Subject;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.SubjectRepository;
import com.shadcn.courseservice.service.IDepartmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService implements IDepartmentService {
    DepartmentRepository departmentRepository;
    SubjectRepository subjectRepository;

    @Override
    public void addSubjectsToDepartment(Long departmentId, List<Long> subjectIds) {
        Department department = getDepartment(departmentId);

        for (Long subjectId : subjectIds) {
            Subject subject = getSubject(subjectId);
            if (department.getSubjects().contains(subject)) {
                throw new AppException(ErrorCode.SUBJECT_EXISTED);
            }
            department.getSubjects().add(subject);
        }

        departmentRepository.save(department);
    }

    @Override
    public void removeSubjectsFromDepartment(Long departmentId, List<Long> subjectIds) {
        Department department = getDepartment(departmentId);
        for (Long subjectId : subjectIds) {
            Subject subject = getSubject(subjectId);
            department.getSubjects().remove(subject);
        }
        departmentRepository.save(department);
    }

    Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    Subject getSubject(Long subjectId) {
        return subjectRepository.findById(subjectId).orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
    }
}
