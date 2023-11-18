package com.sunbase.api.Controller;

import com.sunbase.api.Entity.Customer;
import com.sunbase.api.Service.ApiService;
import com.sunbase.api.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;

@Controller
public class ApiController {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private ApiService apiService;
    private String accessToken;
    private static Customer[] customers;

    @RequestMapping("/add-customer")
    public String addCustomerPage(){
        System.out.println("Inside Add Customer Page Controller");
        return "addCustomer";
    }

    @GetMapping("/update/{id}")
    public String updateCustomerPage(@PathVariable String id, Model model){
        System.out.println("Inside Update Customer Page Controller");
        System.out.println(id);
        Customer customer = null;
        customer =  Arrays.asList(customers).stream().filter(e->e.getUuid().equals(id)).findFirst().get();
        model.addAttribute("customer", customer);
        return "updateCustomer";
    }

    @PostMapping(value = "/authenticate")
    public RedirectView authenticate (
            @RequestParam("login_id") String loginId,
            @RequestParam("password") String password,
            Model model, HttpServletRequest request
    ) {
        System.out.println("Inside Authenticate Controller");

        accessToken = authService.getAccessToken(loginId, password);
        System.out.println(accessToken);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath()+"/customers");
        return redirectView;
    }

    @RequestMapping("/customers")
    public String homePage(Model model){
        System.out.println("Inside Customers Controller");

        ResponseEntity<Customer[]> response = apiService.getCustomersList(accessToken);

        if (response.getStatusCode().is2xxSuccessful()) {
            customers = response.getBody();
            model.addAttribute("customers", Arrays.asList(customers));

        } else {
            model.addAttribute("error", "Error fetching customers");
        }
        return "index";
    }

    @GetMapping("/handle-customer")
    public RedirectView addCustomer(@ModelAttribute Customer customer, HttpServletRequest request){
        System.out.println("Inside Handle Customer to Add Controller");
        System.out.println(customer);
        ResponseEntity<String> response = apiService.addCustomer(customer, accessToken);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath()+"/customers");
        return redirectView;
    }

    @PostMapping("/handle-customer")
    public RedirectView updateCustomer(@ModelAttribute Customer customer, HttpServletRequest request){
        System.out.println("Inside Handle Customer to Update Controller");
        System.out.println(customer);
        String updateUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid="+customer.getUuid();
        ResponseEntity<String> response = apiService.updateCustomer(customer, updateUrl, accessToken);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath()+"/customers");
        return redirectView;
    }


    @RequestMapping("/delete/{id}")
    public RedirectView delete(@PathVariable String id, HttpServletRequest request){
        System.out.println("Inside Delete Controller");
        System.out.println(id);
        id = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid="+id;
        ResponseEntity<String> response = apiService.deleteCustomer(id, accessToken);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath()+"/customers");
        return redirectView;
    }
}
