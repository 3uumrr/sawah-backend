package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "driver_profile")
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_type" , length = 20)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "vehicle_model" , length = 200)
    private String vehicleModel;

    @Column(name = "vehicle_capacity")
    private Integer vehicleCapacity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt =  LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "provider_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
