package com.shadcn.courseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shadcn.courseservice.dto.request.AcademicYearCreation;
import com.shadcn.courseservice.dto.request.AcademicYearUpdation;
import com.shadcn.courseservice.dto.response.AcademicYearResponse;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.entity.AcademicYear;
import com.shadcn.courseservice.entity.Department;
import com.shadcn.courseservice.entity.Semester;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.AcademicYearMapper;
import com.shadcn.courseservice.repository.AcademicYearRepository;
import com.shadcn.courseservice.repository.DepartmentRepository;
import com.shadcn.courseservice.repository.SemesterRepository;
import com.shadcn.courseservice.service.IAcademicYearService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcademicYearService implements IAcademicYearService {
    AcademicYearRepository academicYearRepository;
    AcademicYearMapper academicYearMapper;
    DepartmentRepository departmentRepository;
    SemesterRepository semesterRepository;

    @Override
    @Transactional
    public void createAcademicYear(AcademicYearCreation academicYearCreation) {
        boolean existAcademicYear = academicYearRepository.existsByStartYearAndEndYearInt(
                academicYearCreation.getStartYear(), academicYearCreation.getEndYear());
        if (existAcademicYear) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_EXISTED);
        }
        AcademicYear savedAcademicYear = academicYearMapper.toAcademicYear(academicYearCreation);

        academicYearRepository.save(savedAcademicYear);
    }

    @Override
    public void updateAcademicYear(AcademicYearUpdation academicYearUpdation) {
        AcademicYear academicYear = academicYearRepository
                .findById(academicYearUpdation.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        boolean existAcademicYear = academicYearRepository.existsByStartYearAndEndYearInt(
                academicYearUpdation.getStartYear(), academicYearUpdation.getEndYear());

        if (existAcademicYear) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_EXISTED);
        }

        academicYearMapper.updateAcademicYear(academicYear, academicYearUpdation);
        academicYearRepository.save(academicYear);
    }

    @Override
    public void addDepartmentToAcademicYear(Long academicYearId, Long departmentId) {
        AcademicYear academicYear = getAcademicYear(academicYearId);

        if (academicYear.getDepartments().contains(getDepartment(departmentId))) {
            throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
        }

        academicYear.getDepartments().add(getDepartment(departmentId));
        academicYearRepository.save(academicYear);
    }

    @Override
    public void removeDepartmentFromAcademicYear(Long academicYearId, Long departmentId) {
        AcademicYear academicYear = getAcademicYear(academicYearId);

        academicYear.getDepartments().remove(getDepartment(departmentId));
        academicYearRepository.save(academicYear);
    }

    @Override
    public void addSemesterToAcademicYear(Long academicYearId, Long semesterId) {
        AcademicYear academicYear = getAcademicYear(academicYearId);
        Semester semester = getSemester(semesterId);

        academicYear.getSemesters().add(getSemester(semesterId));
        semester.setAcademicYear(academicYear);

        academicYearRepository.save(academicYear);
        semesterRepository.save(semester);
    }

    @Override
    public void removeSemesterFromAcademicYear(Long academicYearId, Long semesterId) {
        AcademicYear academicYear = getAcademicYear(academicYearId);
        Semester semester = getSemester(semesterId);

        if(semester.getAcademicYear() == null) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }

        academicYear.getSemesters().remove(getSemester(semesterId));
        semester.setAcademicYear(null);

        academicYearRepository.save(academicYear);
        semesterRepository.save(semester);
    }

    @Override
    public PageResponse<AcademicYearResponse> getAllAcademicYears(int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<AcademicYear> academicYears = academicYearRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(
                academicYears, academicYearMapper::toAcademicYearResponse, current);
    }

    AcademicYear getAcademicYear(Long academicYearId) {
        return academicYearRepository
                .findById(academicYearId)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
    }

    Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    Semester getSemester(Long semesterId) {
        return semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
    }
}
