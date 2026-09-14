# 📊 Entity Relationship Diagram (ERD)

<p align="center">
  <img src="diagrams/erd.png" alt="Sawah ERD — 24 entities with full relationships" width="900">
</p>

## Entity Catalog

The Sawah database consists of **24 JPA entities** organized across 6 domains.

---

### 👤 Users & Authentication

#### `User`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK, Auto-increment |
| `firstName` | `String` | Not null |
| `lastName` | `String` | Not null |
| `email` | `String` | Unique, Not null |
| `password` | `String` | Not null |
| `country` | `String` | — |
| `phoneNumber` | `String` | — |
| `gender` | `Enum` | `MALE`, `FEMALE` |
| `profilePictureUrl` | `String` | — |
| `preferredLanguage` | `Enum` | `AR`, `EN` |
| `accountStatus` | `Enum` | `Active`, `InActive` |
| `isProfileComplete` | `Boolean` | — |
| `createdAt` | `LocalDateTime` | Not null, Immutable |
| `updatedAt` | `LocalDateTime` | Not null |

**Relationships:** Many-to-Many → `Role` (via `UserRole`)

#### `Role`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `name` | `Enum` | `TOURIST`, `SERVICE_PROVIDER`, `ADMIN` |
| `createdAt` | `LocalDateTime` | — |

#### `RefreshToken`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `token` | `String` | Unique |
| `expiryDate` | `LocalDateTime` | Not null |
| `userId` | `Long` | FK → User |

---

### 🧑‍💼 Providers

#### `Provider`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `bio` | `TEXT` | — |
| `nationalId` | `String(14)` | Unique, Not null |
| `nationalIdFrontUrl` | `String` | Not null |
| `nationalIdBackUrl` | `String` | Not null |
| `experienceYears` | `Integer` | Min(1), Not null |
| `ratePerHour` | `BigDecimal(10,2)` | — |
| `ratePerDay` | `BigDecimal(10,2)` | — |
| `isAvailable` | `Boolean` | Default: `true` |
| `accountStatus` | `Enum` | `PENDING`, `APPROVED`, `REJECTED` |
| `approvedAt` | `LocalDateTime` | — |
| `rejectedAt` | `LocalDateTime` | — |
| `rejectionReason` | `TEXT` | — |
| `averageRating` | `BigDecimal(3,2)` | Default: `0.00` |
| `totalReviews` | `Integer` | Default: `0` |
| `totalBookings` | `Integer` | Default: `0` |
| `completedBookings` | `Integer` | Default: `0` |
| `createdAt` | `LocalDateTime` | Not null, Immutable |
| `updatedAt` | `LocalDateTime` | Not null |

**Relationships:** One-to-One → `User`, Many-to-One → `Service`

#### `DriverProfile`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `vehicleType` | `Enum` | `SEDAN`, `SUV`, `VAN` |
| `vehicleModel` | `String` | — |
| `vehicleCapacity` | `Integer` | — |
| `createdAt` | `LocalDateTime` | — |
| `updatedAt` | `LocalDateTime` | — |

**Relationships:** One-to-One → `Provider`

#### `ProviderLanguage`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `proficiencyLevel` | `Enum` | `BEGINNER`, `INTERMEDIATE`, `FLUENT` |
| `createdAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `Provider`, Many-to-One → `Language`

#### `ProviderReview`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `stars` | `Integer` | 1–5 |
| `comment` | `String` | — |
| `createdAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `Provider`, Many-to-One → `User` (tourist), Many-to-One → `ServiceRequest`

---

### 📍 Places

#### `Place`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `nameAr` | `String` | Not null |
| `nameEn` | `String` | Not null |
| `governorateAr` | `String` | — |
| `governorateEn` | `String` | — |
| `descriptionAr` | `String` | — |
| `descriptionEn` | `String` | — |
| `openTime` | `LocalTime` | — |
| `closeTime` | `LocalTime` | — |
| `longitude` | `BigDecimal` | — |
| `latitude` | `BigDecimal` | — |
| `averageRating` | `BigDecimal` | — |
| `totalReviews` | `Integer` | — |
| `mainImageUrl` | `String` | — |
| `bookingUrl` | `String` | — |
| `createdAt` | `LocalDateTime` | — |
| `updatedAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `Category`, One-to-Many → `PlacePhoto`, One-to-Many → `PlacePrice`

#### `PlacePhoto`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `url` | `String` | Not null |
| `displayOrder` | `Integer` | — |
| `uploadedAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `Place`

#### `PlacePrice`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `visitorCategoryEn` | `Enum` | `ADULT`, `CHILD`, `STUDENT` |
| `visitorNationalityEn` | `Enum` | `EGYPTIAN`, `FOREIGNER` |
| `visitorCategoryAr` | `Enum` | Arabic equivalent |
| `visitorNationalityAr` | `Enum` | Arabic equivalent |
| `price` | `BigDecimal` | — |

**Relationships:** Many-to-One → `Place`

#### `Category`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `nameAr` | `String` | — |
| `nameEn` | `String` | — |
| `iconUrl` | `String` | — |
| `displayOrder` | `Integer` | — |
| `createdAt` | `LocalDateTime` | — |
| `updatedAt` | `LocalDateTime` | — |

#### `Review`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `stars` | `Integer` | 1–5 |
| `content` | `String` | — |
| `createdAt` | `LocalDateTime` | — |
| `updatedAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `Place`, Many-to-One → `User`

---

### 📅 Bookings & Services

#### `ServiceRequest`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `bookingDateTime` | `LocalDateTime` | Not null |
| `durationHours` | `Integer` | — |
| `durationDays` | `Integer` | — |
| `numberOfPeople` | `Integer` | Not null |
| `translationLanguage` | `String(50)` | — |
| `preferredVehicleType` | `Enum` | `SEDAN`, `SUV`, `VAN` |
| `additionalNotes` | `TEXT` | — |
| `pickupLatitude` | `Double` | — |
| `pickupLongitude` | `Double` | — |
| `totalPrice` | `BigDecimal(10,2)` | Not null |
| `status` | `Enum` | `PENDING`, `ACCEPTED`, `REJECTED`, `WAITING_FOR_CONFIRMATION`, `COMPLETED`, `CANCELLED` |
| `providerResponseMessage` | `TEXT` | — |
| `acceptedAt` | `LocalDateTime` | — |
| `rejectedAt` | `LocalDateTime` | — |
| `completedAt` | `LocalDateTime` | — |
| `cancelledAt` | `LocalDateTime` | — |
| `createdAt` | `LocalDateTime` | Not null, Immutable |
| `updatedAt` | `LocalDateTime` | — |

**Relationships:** Many-to-One → `User` (tourist), Many-to-One → `Place`, Many-to-One → `Provider`, Many-to-One → `Service`

#### `Service`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `code` | `Enum` | `GUIDE`, `TRANSLATOR`, `DRIVER` |
| `nameAr` | `String` | — |
| `nameEn` | `String` | — |
| `createdAt` | `LocalDateTime` | — |

---

### 🔍 Discovery & Personalization

#### `FavoritePlace`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `userId` | `Long` | FK → User |
| `placeId` | `Long` | FK → Place |
| `createdAt` | `LocalDateTime` | — |

#### `VisitedPlace`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `userId` | `Long` | FK → User |
| `placeId` | `Long` | FK → Place |
| `visitedAt` | `LocalDateTime` | — |

#### `RecentSearch`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `createdAt` | `LocalDateTime` | — |
| `placeId` | `Long` | FK → Place |
| `userId` | `Long` | FK → User |

#### `UserPreference`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | Composite (`categoryId`, `userId`) | PK |
| `categoryId` | `Long` | FK → Category |
| `userId` | `Long` | FK → User |
| `createdAt` | `LocalDateTime` | — |

---

### 💬 Communication

#### `ChatConversation`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `chatTitle` | `String` | — |
| `createdAt` | `LocalDateTime` | — |
| `updatedAt` | `LocalDateTime` | — |
| `userId` | `Long` | FK → User |

#### `ChatMessage`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `sender` | `Enum` | `USER`, `ASSISTANT` |
| `message` | `String` | — |
| `createdAt` | `LocalDateTime` | — |
| `conversationId` | `Long` | FK → ChatConversation |

#### `Notification`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `titleKey` | `String` | i18n message key |
| `bodyKey` | `String` | i18n message key |
| `bodyArgs` | `String` | JSON-serialized args |
| `isRead` | `Boolean` | Default: `false` |
| `createdAt` | `LocalDateTime` | — |
| `userId` | `Long` | FK → User |
| `bookingId` | `Long` | FK → ServiceRequest |

---

### 🛡️ Moderation

#### `Issue`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `issueStatus` | `Enum` | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` |
| `description` | `String` | — |
| `issueNumber` | `String` | — |
| `bookingId` | `Long` | FK → ServiceRequest |
| `createdAt` | `LocalDateTime` | — |

#### `Language`
| Column | Type | Constraints |
|--------|------|------------|
| `id` | `Long` | PK |
| `nameAr` | `String` | — |
| `nameEn` | `String` | — |
| `code` | `String` | ISO code |
| `createdAt` | `LocalDateTime` | — |
