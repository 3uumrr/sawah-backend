package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.ServiceCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_code" , nullable = false , length = 50, unique = true)
    @Enumerated(EnumType.STRING)
    private ServiceCode code;

    @Column(name = "name_ar" , nullable = false , length = 50)
    private String nameAr;

    @Column(name = "name_en" , nullable = false , length = 50)
    private String nameEn;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
