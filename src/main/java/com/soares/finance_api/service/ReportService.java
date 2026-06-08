package com.soares.finance_api.service;

import com.opencsv.CSVWriter;
import com.soares.finance_api.dto.ReportResponse;
import com.soares.finance_api.model.Transaction;
import com.soares.finance_api.model.TransactionType;
import com.soares.finance_api.repository.TransactionRepository;
import com.soares.finance_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ReportResponse getSummary(LocalDate start, LocalDate end) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var transactions = transactionRepository.findByUserIdAndDateBetween(
                user.getId(), start, end);

        var totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ReportResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();
    }

    public byte[] exportCsv() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

        try {
            var outputStream = new ByteArrayOutputStream();
            var writer = new CSVWriter(new OutputStreamWriter(outputStream));

            writer.writeNext(new String[]{"ID", "Descrição", "Valor", "Tipo", "Categoria", "Data"});

            for (Transaction t : transactions) {
                writer.writeNext(new String[]{
                        t.getId().toString(),
                        t.getDescription(),
                        t.getAmount().toString(),
                        t.getType().toString(),
                        t.getCategory().getName(),
                        t.getDate().toString()
                });
            }

            writer.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar CSV", e);
        }
    }
}