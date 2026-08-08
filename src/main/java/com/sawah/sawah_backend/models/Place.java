package com.sawah.sawah_backend.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "places", indexes = {
        @Index(name = "uq_places_name_en", columnList = "name_en", unique = true),
        @Index(name = "uq_places_name_ar", columnList = "name_ar", unique = true)
})
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

    @Column(nullable = false, precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;;

    @Column(nullable = false , name = "total_reviews")
    @Builder.Default
    private Integer totalReviews  = 0;

    @Column(name = "main_image_url")
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
    @JoinColumn(name = "category_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Category category;


    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


}
