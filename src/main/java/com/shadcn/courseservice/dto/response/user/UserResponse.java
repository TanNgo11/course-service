package com.shadcn.courseservice.dto.response.user;

import java.util.Date;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class UserResponse {
    String username;
    String firstName;
    String lastName;
    Date dateOfBirth;
    String gender;
    String phoneNumber;
    String address;
    String email;
    String avatar;
    Set<String> roles;
    String status;
    String departmentId;
    Long id;
}
