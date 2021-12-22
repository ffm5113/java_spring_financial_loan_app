package com.example.ist412se_group1_efinance.service;

import com.example.ist412se_group1_efinance.model.Loan;
import org.springframework.data.domain.Page;
import java.util.List;

public interface LoanService {
    List<Loan> getAllLoans();
    void saveLoan(Loan loan);
    Loan getLoanById(long id);
    void deleteLoanById(long id);
    Page<Loan> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
