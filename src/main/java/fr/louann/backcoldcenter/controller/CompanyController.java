package fr.louann.backcoldcenter.controller;

import fr.louann.backcoldcenter.models.Company;
import fr.louann.backcoldcenter.models.Ticket;
import fr.louann.backcoldcenter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*") // Permet au frontend d'accéder
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        if (company.getStatus() == null) {
            company.setStatus("active"); // Défaut à "Actif"
        }
        return companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        return companyService.updateCompany(id, updatedCompany);
    }

    @GetMapping("/{id}/tickets")
    public List<Ticket> getCompanyTickets(@PathVariable Long id) {
        return companyService.getCompanyTickets(id);
    }


}