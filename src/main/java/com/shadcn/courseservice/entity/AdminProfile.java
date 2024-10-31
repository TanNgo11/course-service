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
@Table(name = "admin_profile")
public class AdminProfile extends BaseEntity implements Serializable {
    @Column(unique = true)
    String adminId;

    @Column(unique = true)
    String username;

    LocalDate hireDate;

    String department;

    String major;

    String workSchedule;

    String address;

    String emergencyContactName;

    String emergencyContactPhoneNumber;

    String firstName;

    String lastName;

    LocalDate dateOfBirth;

    @Column(unique = true)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String email;

    String avatarPath;
}
