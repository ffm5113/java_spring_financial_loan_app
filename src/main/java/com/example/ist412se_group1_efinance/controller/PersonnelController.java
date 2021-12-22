package com.example.ist412se_group1_efinance.controller;
// Local project imports
import com.example.ist412se_group1_efinance.model.LoanApp;
import com.example.ist412se_group1_efinance.service.LoanAppService;
import com.example.ist412se_group1_efinance.model.Personnel; // Personnel model class
import com.example.ist412se_group1_efinance.service.PersonnelService; // PersonnelService interface
// Loan app processing by personnel

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
// MISC imports
import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonnelController {
    @Autowired
    private PersonnelService personnelService; // To access list of all personnel
    @Autowired
    private LoanAppService loanAppService;

    Boolean personnelAuthentication = false;
    private Personnel currentPersonnel; // Personnel authenticated upon login

    @PostMapping("/logoutPersonnel")
    public String logoutPersonnel(){
        setCurrentPersonnel(null);
        setPersonnelNotAuthenticated();
        return "redirect:/";
    }

    @GetMapping("/showPersonnelLoginForm")
    public String showLoginForm(Model model) {
        Personnel personnel = new Personnel();
        // Personnel object passed to template for login authentication
        model.addAttribute("personnel", personnel);
        return "personnel_login";
    }

    @RequestMapping("/showPersonnelList")
    public String showPersonnelList(Model model) {
        model.addAttribute("listPersonnel", personnelService.getAllPersonnel());
        // Pass authenticated personnel object to personnel list template for welcome message
        model.addAttribute("personnel", this.getCurrentPersonnel());
        model.addAttribute("pId", getCurrentPersonnel().getpId());
        return "personnel_list";
    }
    @GetMapping("/showNewPersonnelForm")
    public String showNewPersonnelForm(Model model) {
            Personnel personnel = new Personnel();
            // Pass personnel object for new personnel template creation via form fields
            model.addAttribute("personnel", personnel);
            return "new_personnel";
    }
    @RequestMapping("/showUpdateForm/")
    public String showUpdateForm(@RequestParam(value="pId") long pId, Model model) {
            Personnel personnel = personnelService.getPersonnelById(pId); // Get personnel from service
            model.addAttribute("personnel", personnel); // Pass personnel object to template for update
            model.addAttribute("pId", pId);
            model.addAttribute("currentPersonnelId", this.getCurrentPersonnel().getpId());
            return "update_personnel";
    }

    @RequestMapping("deletePersonnel/")
    public String deletePersonnel(@RequestParam (value = "pId") long pId) {
            // Method to delete personnel is called via PersonnelService object
            this.personnelService.deletePersonnelById(pId);
            return "redirect:/showPersonnelList/?pId=" + getCurrentPersonnel().getpId();
//        return "redirect:/showPersonnelList";

    }
    @RequestMapping("/showPersonnelLoanDashboard/")
    public String showPersonnelLoanDashboard(@RequestParam(value="pId") long pId, Model model) {
        Personnel personnel = personnelService.getPersonnelById(pId);
        List<LoanApp> allLoanApps = loanAppService.getAllLoanApps();
        ArrayList<LoanApp> loanAppArrayListToWork = new ArrayList();
        ArrayList<LoanApp> loanAppArrayListWorked = new ArrayList();
        ArrayList<LoanApp> loanAppArrayList = new ArrayList(allLoanApps);
        for (LoanApp l : loanAppArrayList) {
            // If loanProcessedPersonnelId is 0, loan app has not yet been approved && if loanAppStatus is also not
            // approved, add loan to processing list (i.e., show loans that are Pending, Documents Requested only)
            if (l.getApprovedPersonnelId() == 0 && !l.getStatus().equals("Not Approved")) {
                loanAppArrayListToWork.add(l);
            }
            else {
                loanAppArrayListWorked.add(l);
            }
        }
        // Pass loan app array list to Personnel Loan Dashboard template
        model.addAttribute("loanAppArrayListWorked", loanAppArrayListWorked);
        model.addAttribute("loanAppArrayListToWork", loanAppArrayListToWork);
        // Pass current personnel attributes to HTML template to access personnel specific data
        model.addAttribute("pId", pId);
        model.addAttribute("personnelFirstName", personnel.getFirstName());
        return "personnel_loan_dashboard";
    }

    @PostMapping("/savePersonnel")
    public String savePersonnel(@ModelAttribute("personnel") Personnel personnel) {
        List<Personnel> personnelList = personnelService.getAllPersonnel();
        for (Personnel p : personnelList) {
            // If toString matches or personnel id already used
            if(p.toString().equals(personnel.toString()) ||
                    p.getEmployeeId().equals(personnel.getEmployeeId())) {
                System.out.println("Personnel already exists");
                // Return update_personnel form if user already exists. Potential to add error message to new template
                return "update_personnel";
            }
        }
        return checkAndSavePersonnel(personnel); // Check personnel details before saving
    }
    @PostMapping("/updatePersonnel")
    public String updatePersonnel(@ModelAttribute("personnel") Personnel personnel) {
        return checkAndSavePersonnel(personnel); // Check personnel details before updating
    }
    @PostMapping ("/login")
    public String login(@ModelAttribute("personnel")Personnel personnel) {
        List<Personnel> listPersonnel = personnelService.getAllPersonnel();
        System.out.println(listPersonnel.size());
        System.out.println(personnel.toString());
        for(Personnel p : listPersonnel) { // Enhanced for loop to check all personnel
            // If login email and password match iteration in personnel list and personnel has Admin permissions
            if(p.getEmail().equals(personnel.getEmail()) &&
                    p.getPassword().equals(personnel.getPassword()) &&
                            p.getAdminPermissions().equals("Y")) {
                setCurrentPersonnel(p); // Authenticate/set the current personnel object
                // Debugging
                System.out.println("Current Personnel: " + getCurrentPersonnel().toString());
                setPersonnelAuthenticated();
                return"redirect:/showPersonnelList/?pId=" + p.getpId(); // Show personnel list upon authentication
//                return"redirect:/showPersonnelList"; // Show personnel list upon authentication
            }
            else if(p.getEmail().equals(personnel.getEmail()) &&
                    p.getPassword().equals(personnel.getPassword()) &&
                            p.getAdminPermissions().equals("N")){
                setCurrentPersonnel(p);
                System.out.println("Current Personnel: " + getCurrentPersonnel());
                setPersonnelAuthenticated();

                return"redirect:/showPersonnelLoanDashboard/?pId=" + getCurrentPersonnel().getpId();
            }
        }
        setPersonnelNotAuthenticated();
        System.out.println("Not authenticated");
        return"personnel_login"; // Return to login form if not authenticated
    }
    public Personnel getCurrentPersonnel(){
        return currentPersonnel;
    }
    public void setCurrentPersonnel(Personnel currentPersonnel) {
        this.currentPersonnel = currentPersonnel;
    }
    public String checkAndSavePersonnel(Personnel personnel) {
        if((personnel.getAdminPermissions().equals("Y")) || // Check admin permission value (must be Y or N)
                (personnel.getAdminPermissions().equals("N"))) {
            // Create a new personnel object and redirect to personnel list table
            personnelService.savePersonnel(personnel);
            return "redirect:/showPersonnelList/?pId=" + getCurrentPersonnel().getpId();
        } // Else return error message and do not save or update
        System.out.println("Admin Permissions must be set to 'Y' or 'N'");
        return "update_personnel";
    }
    public void setPersonnelAuthenticated(){
        personnelAuthentication = true;
    }
    public void setPersonnelNotAuthenticated(){
        personnelAuthentication = false;
    }

    public String errorRedirectHome(){
        System.out.println("Not authenticated to view this page");
        return "redirect:/";
    }
}