package com.sawah.sawah_backend.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "visited_places")
public class VisitedPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @Column(name = "visited_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime visitedAt = LocalDateTime.now();
}
