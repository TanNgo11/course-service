package com.shadcn.courseservice.dto.request.Lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateLessonRequest {
    Long id;
    String title;
    String description;
    boolean isPublished;
    MultipartFile[] files;
}
