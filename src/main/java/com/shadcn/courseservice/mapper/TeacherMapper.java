package com.shadcn.courseservice.mapper;
import java.util.List;

import com.shadcn.courseservice.dto.response.TeacherInformationDTO;
import com.shadcn.courseservice.dto.response.TeacherProfileResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TeacherMapper {
    
    TeacherInformationDTO toTeacherInfo(TeacherProfileResponse profile);
}
