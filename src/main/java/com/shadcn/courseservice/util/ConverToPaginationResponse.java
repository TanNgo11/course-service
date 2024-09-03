package com.shadcn.courseservice.util;

import java.util.function.Function;

import org.springframework.data.domain.Page;

import com.shadcn.courseservice.dto.response.PageResponse;

public class ConverToPaginationResponse {
    public static <T, R> PageResponse<R> toPageResponse(Page<T> pageData, Function<T, R> mapper, int currentPage) {
        return new PageResponse<>(
                currentPage,
                pageData.getSize(),
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.getContent().stream().map(mapper).toList());
    }
}
