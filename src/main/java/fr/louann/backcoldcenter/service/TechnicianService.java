package fr.louann.backcoldcenter.service;

import fr.louann.backcoldcenter.models.Technician;
import fr.louann.backcoldcenter.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicianService {

    @Autowired
    private TechnicianRepository TechnicianRepository;

    public List<Technician> getAllTechnicians() {
        return TechnicianRepository.findAll();
    }

    public Technician createTechnician(Technician technician) {
        return TechnicianRepository.save(technician);
    }

    public List<Technician> findByCompanyId(Long companyId) {
        return TechnicianRepository.findByCompany_Id(companyId);
    }

    public Technician saveTechnician(Technician technician) {
        return TechnicianRepository.save(technician); // ✅ Sauvegarde en base
    }
}