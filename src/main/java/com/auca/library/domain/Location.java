package com.auca.library.domain;

import com.auca.library.domain.enums.LocationType;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
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
}
