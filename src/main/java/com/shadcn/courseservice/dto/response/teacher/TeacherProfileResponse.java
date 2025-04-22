package com.shadcn.courseservice.dto.response.teacher;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shadcn.courseservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherProfileResponse extends UserProfileResponse {
    String teacherId;

    String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate hireDate;

    String departmentId;

    Double salary;

    String officeHours;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;

    String middleName;

    String lastName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String email;

    String avatarPath;
}
