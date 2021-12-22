package com.example.ist412se_group1_efinance.controller;
// Local project imports
import com.example.ist412se_group1_efinance.model.Loan;
import com.example.ist412se_group1_efinance.model.LoanApp;
import com.example.ist412se_group1_efinance.service.LoanService;
// Spring imports
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
import org.springframework.stereotype.Controller; // Controller class annotation

@Controller
public class LoanController {
    @Autowired
    LoanService loanService;

    public Loan createLoan(LoanApp loanApp){
        Loan loan = new Loan();
        loan.setlId(loanApp.getlAId());
        loan.setPrincipal(loanApp.getPrincipal());
        loan.setAPR(loanApp.getAPR());
        loan.setTerm(loanApp.getTerm());
        loan.setCustomers(loanApp.getCustomers());
        loan.setStartDate(loanApp.getRequestedStartDate());
        loan.setPersonnelId(loanApp.getApprovedPersonnelId());
        loan.calculateLoanValues();
        System.out.println("Approved: " + loan.toString());
//        loanService.saveLoan(loan);
        return loan;
    }
}
