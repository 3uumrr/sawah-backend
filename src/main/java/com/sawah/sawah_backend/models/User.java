package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.Gender;
import com.sawah.sawah_backend.enums.PreferredLanguage;
import com.sawah.sawah_backend.enums.UserAccStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
@Table(name = "users" , indexes = {
        @Index(name = "idx_user_email" , columnList = "email" , unique = true),
        @Index(name = "idx_user_phone" , columnList = "phone_number" , unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name" , nullable = false , length = 100)
    private String firstName;

    @Column(name = "last_name" , nullable = false , length = 100)
    private String lastName;

    @Column(name = "email" , nullable = false , length = 100)
    private String email;

    @Column(name = "password" , nullable = false , length = 255)
    private String password;

    @Column(name = "country" , length = 100)
    private String country;

    @Column(name = "phone_number" , length = 20)
    private String phoneNumber;

    @Column(name = "gender", length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_picture_url" , nullable = true)
    private String profilePictureUrl;

    @Column(name = "preferred_language")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PreferredLanguage preferredLanguage = PreferredLanguage.EN;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserAccStatus accountStatus = UserAccStatus.ACTIVE;

    @Column(name = "is_profile_complete" , nullable = false)
    @Builder.Default
    private Boolean isProfileComplete = false;

    @Column(name = "created_at" , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @ManyToMany(cascade = {CascadeType.MERGE} ,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
