package com.wildboar.service.mapper;

import com.wildboar.domain.*;
import com.wildboar.service.dto.ParentStudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ParentStudent and its DTO ParentStudentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, StudentMapper.class})
public interface ParentStudentMapper extends EntityMapper<ParentStudentDTO, ParentStudent> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    ParentStudentDTO toDto(ParentStudent parentStudent);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "studentId", target = "student")
    ParentStudent toEntity(ParentStudentDTO parentStudentDTO);

    default ParentStudent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ParentStudent parentStudent = new ParentStudent();
        parentStudent.setId(id);
        return parentStudent;
    }
}
