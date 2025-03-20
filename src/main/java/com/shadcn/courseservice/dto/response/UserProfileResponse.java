package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.enums.Gender;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;

    String username;

    String email;

    String avatarPath;

    String firstName;
    String middleName;
    String lastName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth; // For teachers

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate enrollmentDate; // For students

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate; // For teachers



    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    // Student-specific fields
    String grade;
    String major;
    String guardianName;
    String guardianPhoneNumber;
    String nation;
    String religion;
    String citizenId;
    String faculty;
    String degreeLevel;
    String schoolYear;
    String present;
    String academicYearId;

    // Teacher-specific fields
    String departmentId;
    Double salary;
    String officeHours;
    String address;
    String emergencyContactName;
    String emergencyContactPhoneNumber;
}
