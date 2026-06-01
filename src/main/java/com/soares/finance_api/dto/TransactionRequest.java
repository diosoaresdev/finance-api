package com.soares.finance_api.dto;

import com.soares.finance_api.model.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    @NotNull @Positive
    private BigDecimal amount;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate date;
    @NotNull
    private TransactionType type;
    @NotNull
    private Long categoryId;
}