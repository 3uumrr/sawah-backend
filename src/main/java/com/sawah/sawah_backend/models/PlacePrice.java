package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.VisitorCategory;
import com.sawah.sawah_backend.enums.VisitorNationality;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "place_prices")
@Table(name = "place_prices", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_place_nationality_category",
                columnNames = {"place_id" , "visitor_category" , "visitor_nationality"}
        )
})
public class PlacePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_category" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorCategory visitorCategory;

    @Column(name = "visitor_nationality" , nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorNationality visitorNationality;

    @Column(nullable = false, precision = 10, scale = 2) // 50.25   total = 10
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id" , nullable = false)
    private Place place;

}
