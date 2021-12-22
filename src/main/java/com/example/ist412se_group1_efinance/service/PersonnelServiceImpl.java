package com.example.ist412se_group1_efinance.service;

import java.util.List;
import java.util.Optional; // Optional used to contain null and/or not-null values
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
import org.springframework.data.domain.Page; // Used to define findPaginated method return type
import com.example.ist412se_group1_efinance.model.Personnel; // Import personnel model class
import com.example.ist412se_group1_efinance.repository.PersonnelRepository; // Import personnel repository class
import org.springframework.beans.factory.annotation.Autowired; // Autowired annotations
import org.springframework.stereotype.Service; // Service annotations
// Used in findPaginated method
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PersonnelServiceImpl implements PersonnelService {
    @Autowired
    private PersonnelRepository personnelRepository;

    @Override
    public List<Personnel> getAllPersonnel() { return personnelRepository.findAll();}

    @Override
    public void savePersonnel(Personnel personnel) { this.personnelRepository.save(personnel);}

    @Override
    public Personnel getPersonnelById(long id) {
        Optional<Personnel> optional = personnelRepository.findById(id);
        Personnel personnel = null;
        if (optional.isPresent()) {
            personnel = optional.get();
        }
        else {
            throw new RuntimeException("Personnel not found for id: " + id);
        }
        return personnel;
    }

    @Override
    public void deletePersonnelById(long id) { this.personnelRepository.deleteById(id);}

    @Override
    public Page<Personnel> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.personnelRepository.findAll(pageable);
    }
}
