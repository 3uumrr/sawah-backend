package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "place_prices",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_place_nationality_category",
                columnNames = {
                        "place_id",
                        "visitor_category_en",
                        "visitor_nationality_en",
                        "visitor_category_ar",
                        "visitor_nationality_ar"
                }
        )
})
public class PlacePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_category_en" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorCategoryEn visitorCategoryEn;

    @Column(name = "visitor_nationality_en" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorNationalityEn visitorNationalityEn;

    @Column(name = "visitor_category_ar" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorCategoryAr visitorCategoryAr;

    @Column(name = "visitor_nationality_ar" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorNationalityAr visitorNationalityAr;

    @Column(nullable = false, precision = 10, scale = 2) // 50.25   total = 10
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

}
