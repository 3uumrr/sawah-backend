package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.LanguageLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "provider_languages")
@Table(uniqueConstraints = {
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
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
