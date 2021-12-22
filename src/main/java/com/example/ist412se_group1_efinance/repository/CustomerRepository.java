package com.example.ist412se_group1_efinance.repository;

import com.example.ist412se_group1_efinance.model.Customer; // Import customer model class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Used in the creation of repositories
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    @Override
    Optional<Customer> findById(Long id);
}
