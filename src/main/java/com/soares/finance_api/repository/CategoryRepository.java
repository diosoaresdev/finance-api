package com.soares.finance_api.repository;

import com.soares.finance_api.model.Category;
import com.soares.finance_api.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);
    List<Category> findByUserIdAndType(Long userId, TransactionType type);
}
