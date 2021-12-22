package com.example.ist412se_group1_efinance.service;

import java.util.List;
import java.util.Optional; // Optional used to contain null and/or not-null values
import com.example.ist412se_group1_efinance.repository.LoanAppRepository;
import org.springframework.data.domain.Page; // Used to define findPaginated method return type
import com.example.ist412se_group1_efinance.model.LoanApp;
import com.example.ist412se_group1_efinance.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
import org.springframework.stereotype.Service; // Service annotations
// Used in findPaginated method
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class LoanAppServiceImpl implements LoanAppService {
    @Autowired
    private LoanAppRepository loanAppRepository;

    @Override
    public List<LoanApp> getAllLoanApps() { return loanAppRepository.findAll();}

    @Override
    public void saveLoanApp(LoanApp loanApp) { this.loanAppRepository.save(loanApp);}

    @Override
    public LoanApp getLoanAppById(long id) {
        Optional<LoanApp> optional = loanAppRepository.findById(id);
        LoanApp loanApp = null;
        if (optional.isPresent()) {
            loanApp = optional.get();
        }
        else {
            throw new RuntimeException("Loan App not found for id: " + id);
        }
        return loanApp;
    }

    @Override
    public void deleteLoanAppById(long id) { this.loanAppRepository.deleteById(id);}

    @Override
    public Page<LoanApp> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.loanAppRepository.findAll(pageable);
    }
}
