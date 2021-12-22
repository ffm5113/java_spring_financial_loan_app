package com.example.ist412se_group1_efinance.service;

import com.example.ist412se_group1_efinance.model.LoanApp;
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page;
import java.util.List;

public interface LoanAppService {
    List<LoanApp> getAllLoanApps();
    void saveLoanApp(LoanApp loanApp);
    LoanApp getLoanAppById(long id);
    void deleteLoanAppById(long id);
    Page<LoanApp> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
