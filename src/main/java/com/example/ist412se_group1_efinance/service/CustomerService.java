package com.example.ist412se_group1_efinance.service;

import com.example.ist412se_group1_efinance.model.Customer;
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    void saveCustomer(Customer customer);
    Customer getCustomerById(long id);
    void deleteCustomerById(long id);
    Page<Customer> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
