package com.sawah.sawah_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
        name = "provider_reviews",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_provider_reviews_service_request",
                columnNames = {"service_request_id"}
        )
})
public class ProviderReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer stars;

    @Column(columnDefinition = "TEXT")
    private String comment;


    @Column(nullable = false , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User tourist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_request_id" , nullable = false)
    private ServiceRequest serviceRequest;

}
