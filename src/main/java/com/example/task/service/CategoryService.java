package com.example.task.service;


import com.example.task.entity.Category;
import com.example.task.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }


}
