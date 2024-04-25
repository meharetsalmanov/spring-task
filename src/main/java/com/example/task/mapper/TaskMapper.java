package com.example.task.mapper;

import com.example.task.entity.Task;
import com.example.task.model.request.TaskRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task taskRequestToTask(TaskRequest request,Long userId);
    Task updateTaskRequestToTask(TaskRequest request,Task task);
}
