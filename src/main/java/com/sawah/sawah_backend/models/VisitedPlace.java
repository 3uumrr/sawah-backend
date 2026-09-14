package com.sawah.sawah_backend.models;

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
@Table(
        name = "visited_places",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_visited_place_user",
                        columnNames = {"user_id", "place_id"}
                )
        }
)
public class VisitedPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @Column(name = "visited_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime visitedAt = LocalDateTime.now();
}
