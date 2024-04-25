package com.example.task.mapper.impl;

import com.example.task.entity.Category;
import com.example.task.entity.Task;
import com.example.task.exception.BaseException;
import com.example.task.model.request.TaskRequest;
import com.example.task.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.task.mapper.TaskMapper;

import static com.example.task.model.enums.response.ErrorResponseMessages.CATEGORY_NOT_FOUND;
import static java.util.Optional.ofNullable;


@Component
@AllArgsConstructor
public class TaskMapperImpl implements TaskMapper {

    private final CategoryService categoryService;
    @Override
    public Task taskRequestToTask(TaskRequest request,Long userId){
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .priority(request.getPriority())
                .userId(userId)
                .build();

        if (request.getCategoryId() != null){
            Category category = categoryService.findById(request.getCategoryId()).orElseThrow(()->
                    BaseException.of(CATEGORY_NOT_FOUND)
            );
            task.setCategory(category);
        }
        return task;
    }

    @Override
    public Task updateTaskRequestToTask(TaskRequest request, Task task) {
        ofNullable(request.getName()).ifPresent(task::setName);
        ofNullable(request.getDescription()).ifPresent(task::setDescription);
        ofNullable(request.getPriority()).ifPresent(task::setPriority);
        ofNullable(request.getDeadline()).ifPresent(task::setDeadline);

        if (request.getCategoryId() != null){
            Category category = categoryService.findById(request.getCategoryId()).orElseThrow(()->
                    BaseException.of(CATEGORY_NOT_FOUND)
            );
            task.setCategory(category);
        }
        return task;
    }
}
