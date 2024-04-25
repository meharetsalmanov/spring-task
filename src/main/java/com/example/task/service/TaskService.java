package com.example.task.service;


import com.example.task.entity.Task;
import com.example.task.exception.BaseException;
import com.example.task.mapper.TaskMapper;
import com.example.task.model.dto.CustomUserDetails;
import com.example.task.model.request.TaskRequest;
import com.example.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.task.model.enums.response.ErrorResponseMessages.TASK_NOT_FOUND;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final TaskMapper mapper;

    public List<Task> tasks(){
        return taskRepository.findAllByUserId(getSignedUserId());
    }

    public void createTask(TaskRequest request){
        Task task = mapper.taskRequestToTask(request,getSignedUserId());
        taskRepository.save(task);
    }

    public void updateTask(Long id,TaskRequest request){
        Task task = mapper.updateTaskRequestToTask(request,fetchTask(id));
        taskRepository.save(task);
    }

    public void completeTask(Long id){
        Task task = fetchTask(id);
        task.setCompletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public void deleteTask(Long id){
        Task task = fetchTask(id);
        taskRepository.delete(task);
    }

    private Long getSignedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  ((CustomUserDetails) authentication.getPrincipal()).getId();
    }

    private Task fetchTask(Long id){
        return taskRepository.findByIdAndUserId(id,getSignedUserId()).orElseThrow( ()-> BaseException.of(TASK_NOT_FOUND) );
    }






}
