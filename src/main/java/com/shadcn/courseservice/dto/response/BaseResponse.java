package com.shadcn.courseservice.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse {
    Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDate createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDate modifiedDate;

    String createdBy;
    String modifiedBy;
}
