package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "providers",
        indexes = {
        @Index(name = "idx_provider_nationalId" , columnList = "national_id" , unique = true),
})
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "national_id", length = 14, nullable = false)
    private String nationalId;

    @Column(name = "national_id_front_url", nullable = false)
    private String nationalIdFrontUrl;

    @Column(name = "national_id_back_url", nullable = false)
    private String nationalIdBackUrl;

    @Column(name = "experience_years" , nullable = false)
    @Min(1)
    private Integer experienceYears;

    @Column(name = "rate_per_hour" , precision = 10 , scale = 2)
    private BigDecimal ratePerHour;

    @Column(name = "rate_per_day" , precision = 10 , scale = 2)
    private BigDecimal ratePerDay;

    @Column(name = "is_available" , nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "account_status" , nullable = false , length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProviderStatus accountStatus = ProviderStatus.PENDING;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "average_rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews", nullable = false)
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "total_bookings", nullable = false)
    @Builder.Default
    private Integer totalBookings = 0;

    @Column(name = "completed_bookings", nullable = false)
    @Builder.Default
    private Integer completedBookings = 0;

    @Column(name = "created_at" , nullable = false , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at" , nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "service_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Service service;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
