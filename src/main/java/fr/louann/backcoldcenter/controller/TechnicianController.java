package fr.louann.backcoldcenter.controller;

import fr.louann.backcoldcenter.models.Company;
import fr.louann.backcoldcenter.models.Technician;
import fr.louann.backcoldcenter.repository.CompanyRepository;
import fr.louann.backcoldcenter.repository.TechnicianRepository;
import fr.louann.backcoldcenter.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/technicians")
@CrossOrigin(origins = "*") // Permet au frontend d'accéder
public class TechnicianController {

    @Autowired
    private TechnicianService TechnicianService;

    @Autowired
    private CompanyRepository CompanyRepository;

    @GetMapping
    public List<Technician> getAllTechnicians() {
        return TechnicianService.getAllTechnicians();
    }

    @PostMapping
    public ResponseEntity<Technician> createTechnician(@RequestBody Technician technician) {
        // Vérifier si une entreprise est bien liée
        if (technician.getCompanyId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Récupérer la société à partir de l'ID
        Optional<Company> companyOpt = CompanyRepository.findById(technician.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Associer la société au technicien
        technician.setCompany(companyOpt.get());

        Technician savedTechnician = TechnicianService.saveTechnician(technician);
        return ResponseEntity.ok(savedTechnician);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Technician>> getTechniciansByCompany(@PathVariable Long companyId) {
        List<Technician> technicians = TechnicianService.findByCompanyId(companyId);
        return ResponseEntity.ok(technicians);
    }
}