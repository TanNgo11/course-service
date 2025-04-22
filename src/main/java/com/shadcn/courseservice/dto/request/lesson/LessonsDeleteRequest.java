package com.shadcn.courseservice.dto.request.lesson;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonsDeleteRequest {
    List<Long> lessonIds;
}
