package fr.louann.backcoldcenter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technicians")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String specialization;
    private String availability;
    private boolean isOnDuty;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Long getCompanyId() {
        return (company != null) ? company.getId() : null;
    }

    @JsonProperty("companyId")
    public void setCompanyId(Long companyId) {
        if (this.company == null) {
            this.company = new Company();
        }
        this.company.setId(companyId);
    }


}
