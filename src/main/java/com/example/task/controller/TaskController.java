package com.example.task.controller;


import com.example.task.entity.Task;
import com.example.task.model.request.TaskRequest;
import com.example.task.model.response.base.BaseResponse;
import com.example.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public BaseResponse<List<Task>> tasks()
    {
        return BaseResponse.success( taskService.tasks() );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@Valid @RequestBody TaskRequest request){
        taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public void updateTask(@Valid @RequestBody TaskRequest request,  @PathVariable Long id){
        taskService.updateTask(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}")
    public void completeTask(@PathVariable Long id)
    {
        taskService.completeTask(id);
    }
}
