package com.shadcn.courseservice.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import com.shadcn.courseservice.enums.Gender;
import com.shadcn.courseservice.enums.Present;
import com.shadcn.courseservice.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "student_profile")
public class StudentProfile extends BaseEntity implements Serializable {
    @Column(unique = true)
    String studentId;

    @Column(unique = true)
    String username;

    String courseId;

    String firstName;

    String lastName;

    String address;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dateOfBirth;

    // @Column(unique = true)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String grade;

    LocalDate enrollmentDate;

    String major;

    String guardianName;

    String guardianPhoneNumber;

    String email;

    String avatarPath;
    // 54 dân tộc :)))
    String nation;

    String religion;

    String citizenId;
    // At the moment just have Information technology and Business Administration
    String faculty;
    // Ex: Đại học chính quy Tiếng Việt K10
    String degreeLevel;

    @Pattern(regexp = "^2\\d{3}-2\\d{3}$", message = "School year must be between 2000-2099")
    String schoolYear;

    @Enumerated(EnumType.STRING)
    Present present;

    @OneToMany(mappedBy = "studentProfile")
    private List<Registration> registrations;
}
