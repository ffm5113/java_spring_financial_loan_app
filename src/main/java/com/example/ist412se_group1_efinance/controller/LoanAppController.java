package com.example.ist412se_group1_efinance.controller;
// Local project imports
import com.example.ist412se_group1_efinance.model.Customer;
import com.example.ist412se_group1_efinance.model.Loan;
import com.example.ist412se_group1_efinance.model.LoanApp;
import com.example.ist412se_group1_efinance.model.Personnel;
import com.example.ist412se_group1_efinance.service.CustomerService;
import com.example.ist412se_group1_efinance.service.LoanService;
import com.example.ist412se_group1_efinance.service.LoanAppService;
// Spring imports
//import com.example.ist412se_group1_efinance.service.LoanService;
import com.example.ist412se_group1_efinance.service.PersonnelService;
// Autowired annotations
import org.springframework.beans.factory.annotation.Autowired; 
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
// Controller class annotation
import org.springframework.stereotype.Controller; 
// Model used as parameter for GetMapping methods
import org.springframework.ui.Model; 
// org.springframework.web.bind.annotation.* imports 
// for web mapping/integration with thymeleaf templates
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

// MISC imports
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LoanAppController {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private CustomerService customerService; // To access list of all customers
    @Autowired
    private LoanAppService loanAppService; // To access list of loan apps
    @Autowired
    private LoanService loanService; // To access list of all loans
    @Autowired
    private PersonnelService personnelService; // To access list of all personnel

     @RequestMapping("/loanAppForm/")
     public String loanAppForm(@RequestParam(value="cId") long customerId, Model model) {
         Customer customer = customerService.getCustomerById(customerId);
         LoanApp loanApp = new LoanApp();
         Set<Customer> customerSet = loanApp.getCustomers();
         // Add current customer to HashSet, passed to loanApp template
         customerSet.add(customer); 
         // Pass model attributes to loanApp request template
         model.addAttribute("customerSet", customerSet);
         model.addAttribute("customer", customer);
         model.addAttribute("loanApp", loanApp);
         model.addAttribute("cId", customer.getcId());
         // Date customer opened app form
         Date date = new Date(); 
         // Calendar used to add days to today's date
         Calendar calendar = Calendar.getInstance(); 
         calendar.setTime(date);
         // Provide 3 days for loan app processing
         calendar.add(Calendar.DAY_OF_MONTH, 3); 
         String formattedDate = dateFormat.format(calendar.getTime());
         model.addAttribute("date", formattedDate);
         return "loan_app_request";
     }

    @RequestMapping("/showLoanAppProcess/")
    public String showLoanAppProcess(@RequestParam(value= "lAId") long lAId,
                                     @RequestParam(value= "pId") long PIDD, Model model) {
        Personnel personnel = personnelService.getPersonnelById(PIDD);
        LoanApp loanApp = loanAppService.getLoanAppById(lAId);
        Loan loan = new Loan();
        System.out.println("Personnel ID: " + PIDD);
        model.addAttribute("PIDD", PIDD);
        model.addAttribute("personnel", personnel);
        model.addAttribute("loanApp", loanApp);
        model.addAttribute("loan", loan);
        return "show_loan_app_process";
    }
    @PostMapping("/loanApp")
    public String loanApp(@ModelAttribute("loanApp") LoanApp loanApp) {
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        System.out.println("Today's loan app date: " + formattedDate);
        loanApp.setDate(formattedDate);
        loanApp.calculateLoanAppIncome();
        loanAppService.saveLoanApp(loanApp);
        System.out.println("Loan App created: " + loanApp.toString());
        return "redirect:/showCustomerAppDashboard/?cId=" + 
            loanApp.getPrimaryCustomer().getcId();
    }
    @PostMapping("/processLoanApp/{l}/{p}")
    public String processLoanApp(@ModelAttribute("loanApp") LoanApp loanApp,
                                 @PathVariable("p") long personnelId) {
        System.out.println("PERSONNEL ID (p): " + personnelId);
        System.out.println("LOAN APP ID (Fro loanApp): " + loanApp.getlAId());
        loanApp.setPersonnelId(personnelId);
        loanApp.setDateOfLastUpdate(formatDate());
        Personnel personnel = personnelService.getPersonnelById(personnelId);
        if(loanApp.getStatus().equals("Approved")) {
            loanApp.setApprovedPersonnelId(personnelId);
            loanApp.setStatus("Approved");
            loanAppService.saveLoanApp(loanApp);
            Loan loan = new Loan();
            loan.setlId(loanApp.getlAId());
            loan.setPrincipal(loanApp.getPrincipal());
            loan.setAPR(loanApp.getAPR());
            loan.setTerm(loanApp.getTerm());
            loan.setCustomers(loanApp.getCustomers());
            loan.setStartDate(loanApp.getRequestedStartDate());
            loan.setPersonnelId(personnelId);
            loan.calculateLoanValues();
            System.out.println("Approved Loan: " + loan.toString());
            loanService.saveLoan(loan);
        }
        else if(loanApp.getStatus().equals("Documents Requested")){
            loanApp.setStatus("Documents Requested");
            System.out.println("Documents Requested: " + loanApp.toString());
            loanAppService.saveLoanApp(loanApp);
        }
        else if(loanApp.getStatus().equals("Not Approved")){
            loanApp.setStatus("Not Approved");
            System.out.println("Loan App Not Approved: " + loanApp.toString());
            loanAppService.saveLoanApp(loanApp);
        }
        else if(loanApp.getStatus().equals("Pending Review")){
            loanApp.setPersonnelId(0);
            loanApp.setDateOfLastUpdate("Not yet processed");
            System.out.println("Pending Review: " + loanApp.toString());
            loanAppService.saveLoanApp(loanApp);
        }
        return "redirect:/showPersonnelLoanDashboard/?pId=" + personnelId;

}
    public String formatDate() {
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
