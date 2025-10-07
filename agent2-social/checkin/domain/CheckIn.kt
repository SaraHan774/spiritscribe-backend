package com.spiritscribe.checkin.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "check_ins")
class CheckIn(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "user_id", nullable = false, length = 36)
    val userId: String,
    
    @Column(name = "whiskey_id", nullable = false, length = 36)
    val whiskeyId: String,
    
    @Column(name = "location", length = 255)
    val location: String? = null,
    
    @Column(name = "location_lat", precision = 10, scale = 8)
    val locationLat: Double? = null,
    
    @Column(name = "location_lng", precision = 11, scale = 8)
    val locationLng: Double? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    val locationType: LocationType = LocationType.OTHER,
    
    @Column(name = "rating", precision = 3, scale = 2)
    val rating: Double? = null,
    
    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String? = null,
    
    @Column(name = "is_public", nullable = false)
    val isPublic: Boolean = true,
    
    @Column(name = "is_featured", nullable = false)
    val isFeatured: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    
    @Column(name = "updated_at")
    val updatedAt: Instant? = null
)

enum class LocationType {
    WHISKEY_BAR, RESTAURANT, HOME, EVENT, OTHER
}
