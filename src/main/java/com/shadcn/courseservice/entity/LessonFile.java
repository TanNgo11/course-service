package com.shadcn.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "lesson_file")
public class LessonFile extends BaseEntity {

    @Column( nullable = false)
    String fileName;

    @Column(nullable = false)
    String filePath;

    String fileType;

    Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    Lesson lesson;
}