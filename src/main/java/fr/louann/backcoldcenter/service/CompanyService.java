package fr.louann.backcoldcenter.service;

import fr.louann.backcoldcenter.models.Company;
import fr.louann.backcoldcenter.models.Ticket;
import fr.louann.backcoldcenter.repository.CompanyRepository;
import fr.louann.backcoldcenter.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    private TicketRepository ticketRepository;

    @Autowired
    public CompanyService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Optional<Company> existingOpt = companyRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Company not found");
        }
        Company existing = existingOpt.get();

        // ✅ Mise à jour de tous les champs
        existing.setName(updatedCompany.getName());
        existing.setAddress(updatedCompany.getAddress());
        existing.setContactPerson(updatedCompany.getContactPerson());
        existing.setEmail(updatedCompany.getEmail());
        existing.setPhone(updatedCompany.getPhone());
        existing.setWebsite(updatedCompany.getWebsite());
        existing.setNotes(updatedCompany.getNotes());
        existing.setContractStartDate(updatedCompany.getContractStartDate());
        existing.setContractEndDate(updatedCompany.getContractEndDate());
        existing.setInterventionStartTime(updatedCompany.getInterventionStartTime());
        existing.setInterventionEndTime(updatedCompany.getInterventionEndTime());
        existing.setStatus(updatedCompany.getStatus()); // ✅ Ajout de la mise à jour du statut

        if (updatedCompany.getBillingInfo() != null) {
            existing.getBillingInfo().setFactName(updatedCompany.getBillingInfo().getFactName());
            existing.getBillingInfo().setFactAddress(updatedCompany.getBillingInfo().getFactAddress());
            existing.getBillingInfo().setVatNumber(updatedCompany.getBillingInfo().getVatNumber());
        }

        if (updatedCompany.getOperatingHours() != null) {
            existing.getOperatingHours().setStartTime(updatedCompany.getOperatingHours().getStartTime());
            existing.getOperatingHours().setEndTime(updatedCompany.getOperatingHours().getEndTime());
        }

        return companyRepository.save(existing);
    }

    public List<Ticket> getCompanyTickets(Long companyId) {
        return ticketRepository.findByCompanyIdWithDetails(companyId);
    }


}