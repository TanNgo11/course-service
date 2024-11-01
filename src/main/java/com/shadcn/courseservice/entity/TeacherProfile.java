package com.shadcn.courseservice.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import com.shadcn.courseservice.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "teacher_profile")
public class TeacherProfile extends BaseEntity implements Serializable {
    @Column(unique = true)
    String teacherId;

    @Column(unique = true)
    String username;

    LocalDate hireDate;

    String department;

    String major;

    Double salary;

    String officeHours;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;

    String lastName;

    LocalDate dateOfBirth;

    //@Column(unique = true)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String email;

    String avatarPath;
}
