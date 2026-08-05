package com.sawah.sawah_backend.models;


import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "service_requests")
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date_time" , nullable = false)
    private LocalDateTime bookingDateTime;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "number_of_people" , nullable = false)
    private Integer numberOfPeople;

    @Column(name = "translation_language" , length = 50)
    private String translationLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_vehicle_type")
    private VehicleType preferredVehicleType;

    @Column(name = "additional_notes" , columnDefinition = "TEXT")
    private String additionalNotes;

    @Column(name = "pickup_latitude")
    private Double pickupLatitude;

    @Column(name = "pickup_longitude")
    private Double pickupLongitude;

    @Column(name = "total_price" , nullable = false ,precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false , length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ServiceRequestStatus status = ServiceRequestStatus.PENDING;

    @Column(name = "provider_response_message" , columnDefinition = "TEXT")
    private String providerResponseMessage;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at" , nullable = false , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id" , nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User tourist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id" , nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id" , nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id" , nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Service service;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
