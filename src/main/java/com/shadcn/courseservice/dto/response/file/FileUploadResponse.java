package com.shadcn.courseservice.dto.response.file;

import java.io.Serializable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadResponse implements Serializable {
    String fileName;
    String downloadUri;
    long size;
}
