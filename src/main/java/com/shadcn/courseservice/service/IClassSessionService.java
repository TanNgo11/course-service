package com.shadcn.courseservice.service;

import com.shadcn.courseservice.dto.request.attendance.class_session.ClassSessionCreationRequest;
import com.shadcn.courseservice.entity.ClassSession;

public interface IClassSessionService {
    ClassSession createClassSession(ClassSessionCreationRequest request);
}
