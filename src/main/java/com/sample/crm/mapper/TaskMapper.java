package com.sample.crm.mapper;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    TaskDTO toDto(Task task);

    Task toEntity(TaskDTO taskDTO);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "contact", ignore = true)
    void update(@MappingTarget Task task, TaskDTO taskDTO);
}
