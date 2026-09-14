package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.LanguageLevel;
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
        name = "provider_languages",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_provider_languages_provider_lang",
                columnNames = {"provider_id","language_id"}
        )
})
public class ProviderLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level" , nullable = false , length = 20)
    private LanguageLevel proficiencyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Language language;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
