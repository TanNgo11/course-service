package com.shadcn.courseservice.constant;

public class PathConstant {
    public static final String API_V1 = "/api/v1";

    public static final String ACADEMIC_YEARS = "/academic-years";

    public static final String DEPARTMENTS = "/departments";

    public static final String API_V1_ACADEMIC_YEARS = API_V1 + ACADEMIC_YEARS;

    public static final String API_V1_DEPARTMENTS = API_V1 + DEPARTMENTS;

    public static final String[] PUBLIC_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs/**"};
}
