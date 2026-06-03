package com.soares.finance_api.service;

import com.soares.finance_api.dto.CategoryRequest;
import com.soares.finance_api.dto.CategoryResponse;
import com.soares.finance_api.model.Category;
import com.soares.finance_api.repository.CategoryRepository;
import com.soares.finance_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CategoryResponse> listAll() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return categoryRepository.findByUserId(user.getId())
                .stream()
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .type(c.getType())
                        .build())
                .toList();
    }

    public CategoryResponse create(CategoryRequest request) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(user)
                .build();

        categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }

    public void delete(Long id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar esta categoria");
        }

        categoryRepository.delete(category);
    }
}