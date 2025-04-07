package com.sample.crm.mapper;

import com.sample.crm.dto.CommentDTO;
import com.sample.crm.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "username", source = "user.username")
    CommentDTO toDto(Comment comment);

    Comment toEntity(CommentDTO commentDTO);

    void update(@MappingTarget Comment comment, CommentDTO commentDTO);
}
