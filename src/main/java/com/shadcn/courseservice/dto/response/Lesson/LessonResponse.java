package com.shadcn.courseservice.dto.response.Lesson;

import java.util.List;

import com.shadcn.courseservice.dto.response.lessonFile.LessonFileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonResponse {
    Long id;
    String title;
    Long courseId;
    String description;
    boolean isPublished;
    List<LessonFileResponse> files;
}
