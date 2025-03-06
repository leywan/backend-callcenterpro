package fr.louann.backcoldcenter.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OperatingHours {
    private LocalTime startTime;
    private LocalTime endTime;
}
