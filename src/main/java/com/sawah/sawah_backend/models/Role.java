package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false , unique = true , length = 20)
    @Enumerated(EnumType.STRING)
    private RoleName name;  // TOURIST, SERVICE_PROVIDER, ADMIN

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
