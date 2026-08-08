package com.auca.library.domain;

import com.auca.library.domain.enums.LocationType;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Location {
    @Id
    private UUID locationId = UUID.randomUUID();
    private String locationCode;
    private String locationName;
    
    @Enumerated(EnumType.STRING)
    private LocationType locationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private Location parent;

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }
}
