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
@Table(name = "place_photos")
public class PlacePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;


    @Column(name = "uploaded_at" , updatable = false , nullable = false)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

}
