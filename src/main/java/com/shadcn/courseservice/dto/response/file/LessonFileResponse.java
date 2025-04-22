package com.shadcn.courseservice.dto.response.file;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonFileResponse {
    String fileName;

    String filePath;

    String fileType;

    Long fileSize;
}
