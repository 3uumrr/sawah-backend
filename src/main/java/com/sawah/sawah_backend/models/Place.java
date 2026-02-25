package com.sawah.sawah_backend.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar" , nullable = false , length = 50)
    private String nameAr;

    @Column(name = "name_en" , nullable = false , length = 50)
    private String nameEn;

    @Column(name = "governorate_ar" , nullable = false , length = 50)
    private String governorateAr;

    @Column(name = "governorate_en" , nullable = false , length = 50)
    private String governorateEn;

    @Column(name = "description_ar" , nullable = false , length = 2000)
    private String descriptionAr;

    @Column(name = "description_en" , nullable = false , length = 2000)
    private String descriptionEn;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(nullable = false, precision = 10, scale = 8) // 31.21535259   total = 10
    private BigDecimal longitude;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "main_image_url" , nullable = false)
    private String mainImageUrl;

    @Column(name = "booking_url", length = 500)
    private String bookingUrl;

    @Column(name = "created_at" , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


}
