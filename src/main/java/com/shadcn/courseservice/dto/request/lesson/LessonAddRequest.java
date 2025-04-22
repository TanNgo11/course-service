package com.shadcn.courseservice.dto.request.lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonAddRequest {
    Long courseId;
    String title;
}
