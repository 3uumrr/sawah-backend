package com.sawah.sawah_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "provider_services")
@Table(name = "provider_services" , uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_provider_services_provider_service",
                columnNames = {"provider_id","service_id"}
        )
})
public class ProviderService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experience_years" , nullable = false)
    @Min(1)
    private Integer experienceYears;

    @Column(name = "rate_per_hour" , precision = 10 , scale = 2)
    private BigDecimal ratePerHour;

    @Column(name = "rate_per_day" , precision = 10 , scale = 2)
    private BigDecimal ratePerDay;

    @Column(name = "isAvailable" , nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
