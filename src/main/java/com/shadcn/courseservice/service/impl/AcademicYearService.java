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
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.AcademicYearMapper;
import com.shadcn.courseservice.repository.AcademicYearRepository;
import com.shadcn.courseservice.service.IAcademicYearService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcademicYearService implements IAcademicYearService {
    AcademicYearRepository academicYearRepository;
    AcademicYearMapper academicYearMapper;

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
    public PageResponse<AcademicYearResponse> getAllAcademicYears(int current, int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<AcademicYear> academicYears = academicYearRepository.findAll(pageable);

        return ConverToPaginationResponse.toPageResponse(
                academicYears, academicYearMapper::toAcademicYearResponse, current);
    }
}
