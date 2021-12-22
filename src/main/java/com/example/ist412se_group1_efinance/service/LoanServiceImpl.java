package com.example.ist412se_group1_efinance.service;

import java.util.List;
import java.util.Optional; // Optional used to contain null and/or not-null values
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page; // Used to define findPaginated method return type
import com.example.ist412se_group1_efinance.model.Loan;
import com.example.ist412se_group1_efinance.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
import org.springframework.stereotype.Service; // Service annotations
// Used in findPaginated method
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> getAllLoans() { return loanRepository.findAll();}

    @Override
    public void saveLoan(Loan loan) { this.loanRepository.save(loan);}

    @Override
    public Loan getLoanById(long id) {
        Optional<Loan> optional = loanRepository.findById(id);
        Loan loan = null;
        if (optional.isPresent()) {
            loan = optional.get();
        }
        else {
            throw new RuntimeException("Loan not found for id: " + id);
        }
        return loan;
    }

    @Override
    public void deleteLoanById(long id) { this.loanRepository.deleteById(id);}

    @Override
    public Page<Loan> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.loanRepository.findAll(pageable);
    }
}
