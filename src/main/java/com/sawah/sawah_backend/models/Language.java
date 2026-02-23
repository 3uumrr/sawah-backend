package com.sawah.sawah_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar" , nullable = false , length = 50)
    private String nameAr;

    @Column(name = "name_en" , nullable = false , length = 50)
    private String nameEn;


    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
