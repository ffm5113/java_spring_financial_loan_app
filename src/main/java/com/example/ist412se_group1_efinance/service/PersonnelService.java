package com.example.ist412se_group1_efinance.service;

import com.example.ist412se_group1_efinance.model.Personnel; // Import course model class
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page;
import java.util.List;

public interface PersonnelService {
    List<Personnel> getAllPersonnel();
    void savePersonnel(Personnel personnel);
    Personnel getPersonnelById(long id);
    void deletePersonnelById(long id);
    Page<Personnel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}

