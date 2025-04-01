package com.shadcn.courseservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND(1010, "Resource not found", HttpStatus.NOT_FOUND),
    FILL_IN_THE_INPUT_FIELD(1011, "Fill in the input field", HttpStatus.BAD_REQUEST),
    RESOURCE_EXISTED(1013, "Resource existed", HttpStatus.BAD_REQUEST),
    CLOUDINARY_DELETE_FAIL(1014, "Delete the old image fail on cloud", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1015, "Role not existed", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_PARAMETER(1019, "Invalid page parameter", HttpStatus.BAD_REQUEST),
    ACADEMIC_YEAR_EXISTED(1020, "Academic year existed", HttpStatus.BAD_REQUEST),
    ACADEMIC_YEAR_NOT_FOUND(1021, "Academic year not found", HttpStatus.BAD_REQUEST),
    DEPARTMENT_EXISTED(1022, "Department already added", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1023, "Department not found", HttpStatus.BAD_REQUEST),
    SEMESTER_EXISTED(1024, "Semester existed", HttpStatus.BAD_REQUEST),
    SEMESTER_NOT_FOUND(1025, "Semester not found", HttpStatus.BAD_REQUEST),
    COURSE_EXISTED(1026, "Course existed", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1027, "Course not found", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND(1028, "Student not found", HttpStatus.BAD_REQUEST),
    TEACHER_NOT_FOUND(1029, "Teacher not found", HttpStatus.BAD_REQUEST),
    IMAGE_UPLOAD_FAILED(1030, "Image upload failed", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1031, "File upload failed", HttpStatus.BAD_REQUEST),
    BASE_COURSE_EXISTED(1032, "Base course existed", HttpStatus.BAD_REQUEST),
    COURSE_EXISTED_IN_SEMESTER(1033, "Course instance existed in this semester", HttpStatus.BAD_REQUEST),
    REGISTRATION_NOT_FOUND(1034, "Registration not found", HttpStatus.BAD_REQUEST),
    REGISTRATION_CANCELLATION_DEADLINE_PASSED(
            1035, "Registration cancellation deadline passed", HttpStatus.BAD_REQUEST),
    REGISTRATION_BEFORE_SEMESTER_START(1036, "Registration before semester start", HttpStatus.BAD_REQUEST),
    INVALID_REGISTRATION_DATE(1037, "Invalid registration date", HttpStatus.BAD_REQUEST),
    BASE_COURSE_NOT_FOUND(1038, "Base course not found", HttpStatus.BAD_REQUEST),
    LESSON_NOT_FOUND(1039, "Lesson not found", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
