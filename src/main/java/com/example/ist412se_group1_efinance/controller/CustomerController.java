package com.example.ist412se_group1_efinance.controller;
// Local project imports
import com.example.ist412se_group1_efinance.model.Customer;
import com.example.ist412se_group1_efinance.model.Loan;
import com.example.ist412se_group1_efinance.model.LoanApp;
import com.example.ist412se_group1_efinance.service.CustomerService;
import com.example.ist412se_group1_efinance.service.LoanService;
import com.example.ist412se_group1_efinance.service.LoanAppService;
// Spring imports
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.stereotype.Controller; // Controller class annotation
import org.springframework.ui.Model; // Model used as parameter for GetMapping methods
// org.springframework.web.bind.annotation.* imports for web mapping/integration with thymeleaf templates
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// MISC imports
import java.util.List;
import java.util.ArrayList;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService; // To access list of all customers
    @Autowired
    private LoanService loanService; // To access list of loans
    @Autowired
    private LoanAppService loanAppService; // To access list of loans
    // Track authenticated customer to pass to dashboard template, etc. Autowired not required for this variable
    private Customer currentCustomer;
    public Customer getCurrentCustomer(){
        return currentCustomer;
    }
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    Boolean customerAuthentication = false;

    @PostMapping("/logoutCustomer")
    public String logoutCustomer(){

        setCurrentCustomer(null);
        setCustomerNotAuthenticated();
        return "redirect:/";
    }
    @GetMapping("/showCustomerLoginForm")
    public String showCustomerLoginForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer); // To use customer object for login authentication
        return "customer_login";
    }
    @GetMapping("/showCustomerSignUpForm")
    public String showCustomerSignUpForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer); // To use form fields to create customer object
        return  "customer_signup";
    }
    @RequestMapping("/showCustomerDashboard/")
    public String showCustomerDashboard(@RequestParam(value="cId") long customerId, Model model) {
            List<Loan> allLoans = loanService.getAllLoans();
            ArrayList<Loan> customerLoanArrayList = new ArrayList();
            ArrayList<Loan> loanArrayList = new ArrayList(allLoans);
            for (Loan l : loanArrayList) {
                System.out.println(l.getCustomers().toString());
                // If Set<Customer> for loan l contains current customer (toString will be printed to string with brackets)
                if (l.getCustomers().toString().contains(("[" + getCurrentCustomer().toString() + "]"))) {
                    //if(l.getCustomers().toString().contains(("["+getCurrentCustomer().toString()+"]"))){
                    customerLoanArrayList.add(l); // There is a customer match inside array list - add loan to array list
                }
            }
            // Pass customer loan array list to Customer Loan Dashboard
            model.addAttribute("customerLoanArrayList", customerLoanArrayList);
            // Pass current customer attribute to HTML template to access customer specific data
            model.addAttribute("customer", this.getCurrentCustomer());
            model.addAttribute("cId", customerId);
            return "customer_loan_dashboard";
    }
    @RequestMapping("/showCustomerAppDashboard/")
    public String showCustomerAppDashboard(@RequestParam(value="cId") long customerId, Model model) {
            Customer customer = customerService.getCustomerById(customerId);
            List<LoanApp> allLoanApps = loanAppService.getAllLoanApps();
            ArrayList<LoanApp> customerLoanAppArrayList = new ArrayList();
            ArrayList<LoanApp> loanAppArrayList = new ArrayList(allLoanApps);
            for (LoanApp l : loanAppArrayList) {
                System.out.println(l.getCustomers().toString());
                // If Set<Customer> for loanApp contains current customer (toString will be printed to string with brackets)
                if (l.getCustomers().toString().contains(("[" + customer.toString() + "]"))) {
                    customerLoanAppArrayList.add(l); // There is a customer match inside array list - add loan to array list
                }
            }
            // Pass customer loanApp array list to Customer Loan App Dashboard
            model.addAttribute("customerLoanAppArrayList", customerLoanAppArrayList);
            // Pass current customer attribute to HTML template to access customer specific data
            model.addAttribute("customer", customer);
            model.addAttribute("cId", customerId);
            return "customer_app_dashboard";
    }
//    @GetMapping("/showCustomerAppDashboard/{customer_id}")
//    public String showCustomerAppDashboard(@PathVariable(value="customer_id") long customerId, Model model) {
//            Customer customer = customerService.getCustomerById(customerId);
//            List<LoanApp> allLoanApps = loanAppService.getAllLoanApps();
//            ArrayList<LoanApp> customerLoanAppArrayList = new ArrayList();
//            ArrayList<LoanApp> loanAppArrayList = new ArrayList(allLoanApps);
//            for (LoanApp l : loanAppArrayList) {
//                System.out.println(l.getCustomers().toString());
//                // If Set<Customer> for loanApp contains current customer (toString will be printed to string with brackets)
//                if (l.getCustomers().toString().contains(("[" + customer.toString() + "]"))) {
//                    customerLoanAppArrayList.add(l); // There is a customer match inside array list - add loan to array list
//                }
//            }
//            // Pass customer loanApp array list to Customer Loan App Dashboard
//            model.addAttribute("customerLoanAppArrayList", customerLoanAppArrayList);
//            // Pass current customer attribute to HTML template to access customer specific data
//            model.addAttribute("customer", customer);
//            return "customer_app_dashboard";
//    }

//    @GetMapping("showLoanAppDetails/{loan_app_id}")
//    public String showLoanAppDetails(@PathVariable(value="loan_app_id") long loanAppId, Model model){
//        model.addAttribute("loanApp", loanAppService.getLoanAppById(loanAppId));
//        return "work_in_progress";
//    }

    @RequestMapping("showLoanAppDetails/")
    public String showLoanAppDetails(@RequestParam(value="loanAppId") long loanAppId, Model model){
        model.addAttribute("loanApp", loanAppService.getLoanAppById(loanAppId));
        model.addAttribute("loanAppId", loanAppId);
        return "work_in_progress";
    }

    @PostMapping ("/customerLogin")
    public String customerLogin(@ModelAttribute("customer")Customer customer) {
        List<Customer> listCustomers = customerService.getAllCustomers();
        // System.out.println(customer.toString());
        for (Customer c : listCustomers) { // Enhanced for loop to check all customers
            // If login email and password match iteration in customer list
            if (c.getEmail().equals(customer.getEmail()) &&
                    c.getPassword().equals(customer.getPassword())) {
                setCurrentCustomer(c); // Authenticate/set the current customer object
                setCustomerAuthenticated();
                customerService.saveCustomer(c);
                System.out.println("Current Customer: " + getCurrentCustomer().toString());
                return "redirect:/showCustomerDashboard/?cId=" + c.getcId(); // Show dashboard upon authentication
//                return "redirect:/showCustomerDashboard/" + c.getcId(); // Show dashboard upon authentication
            }
        }
        System.out.println("Not authenticated");
        return"customer_login"; // Return to login form if not authenticated
    }

    @PostMapping ("/customerSignUp")
    public String customerSignUp(@ModelAttribute("customer") Customer customer) {
        List<Customer> customerList = customerService.getAllCustomers();
        for(Customer c : customerList) {
            if(c.getEmail().equals(customer.getEmail())) {
                System.out.println("Customer email already in use");
                // Can potentially add a different template with error message. Currently redirects to same form
                // if email is already in use
                return "redirect:/showCustomerSignUpForm";
            }
        }
        customerService.saveCustomer(customer); // Save customer if email/id not already in use
        // Return to login so customer can log in to dashboard with new credentials
        return "redirect:/showCustomerLoginForm";
    }
    public void setCustomerAuthenticated(){
        customerAuthentication = true;
    }
    public void setCustomerNotAuthenticated(){
        customerAuthentication = false;
    }
}
