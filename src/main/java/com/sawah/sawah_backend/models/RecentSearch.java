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
@Table(name = "recent_searches")
public class RecentSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @Column(name = "created_at", nullable = false, updatable = true)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
