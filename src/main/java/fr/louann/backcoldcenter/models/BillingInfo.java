package fr.louann.backcoldcenter.models;

import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BillingInfo {
    private String factName;        // ➡️ Correspond à "billing_name"
    private String factAddress;     // ➡️ Correspond à "billing_address"
    private String vatNumber;   // ➡️ Correspond à "billing_vat_number"
}
