package com.example.ist412se_group1_efinance.service;

import java.util.List;
import java.util.Optional; // Optional used to contain null and/or not-null values
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page; // Used to define findPaginated method return type
import com.example.ist412se_group1_efinance.model.Customer;
import com.example.ist412se_group1_efinance.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
import org.springframework.stereotype.Service; // Service annotations
// Used in findPaginated method
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() { return customerRepository.findAll();}

    @Override
    public void saveCustomer(Customer customer) { this.customerRepository.save(customer);}

    @Override
    public Customer getCustomerById(long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        Customer customer = null;
        if (optional.isPresent()) {
            customer = optional.get();
        }
        else {
            throw new RuntimeException("Customer not found for id: " + id);
        }
        return customer;
    }

    @Override
    public void deleteCustomerById(long id) { this.customerRepository.deleteById(id);}

    @Override
    public Page<Customer> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.customerRepository.findAll(pageable);
    }
}
