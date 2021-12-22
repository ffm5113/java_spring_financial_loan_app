package com.example.ist412se_group1_efinance.repository;

import com.example.ist412se_group1_efinance.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Used in the creation of repositories
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{
/*    @Override
    Optional<Loan> findById(Long id);*/
}
