package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.domain.Location;
import com.auca.library.domain.enums.LocationType;
import com.auca.library.domain.User;
import com.auca.library.dao.UserDao; // Will create next
import java.util.UUID;

public class LocationService {
    private final LocationDao locationDao;
    private final UserDao userDao;

    public LocationService(LocationDao locationDao, UserDao userDao) {
        this.locationDao = locationDao;
        this.userDao = userDao;
    }

    public void createLocation(Location location, UUID parentId) {
        if (locationDao.findByCode(location.getLocationCode()) != null) {
            throw new IllegalArgumentException("Location code already exists");
        }

        if (location.getLocationType() == LocationType.PROVINCE) {
            if (parentId != null) {
                throw new IllegalArgumentException("Province cannot have a parent");
            }
        } else {
            if (parentId == null) {
                throw new IllegalArgumentException("Non-province location must have a parent");
            }
            Location parent = locationDao.findById(parentId);
            if (parent == null) {
                throw new IllegalArgumentException("Parent location not found");
            }

            boolean validParent = false;
            switch (location.getLocationType()) {
                case DISTRICT: validParent = parent.getLocationType() == LocationType.PROVINCE; break;
                case SECTOR: validParent = parent.getLocationType() == LocationType.DISTRICT; break;
                case CELL: validParent = parent.getLocationType() == LocationType.SECTOR; break;
                case VILLAGE: validParent = parent.getLocationType() == LocationType.CELL; break;
                default: break;
            }

            if (!validParent) {
                throw new IllegalArgumentException("Invalid parent location type");
            }
            location.setParent(parent);
        }

        locationDao.save(location);
    }

    public String getProvinceNameByVillageId(UUID villageId) {
        Location village = locationDao.findById(villageId);
        if (village == null || village.getLocationType() != LocationType.VILLAGE) {
            throw new IllegalArgumentException("Invalid village ID");
        }
        Location current = village;
        while (current != null && current.getLocationType() != LocationType.PROVINCE) {
            current = current.getParent();
            if (current != null) {
                current = locationDao.findById(current.getLocationId());
            }
        }
        return current != null ? current.getLocationName() : null;
    }

    public String getProvinceNameByPersonId(UUID personId) {
        User user = userDao.findById(personId);
        if (user == null || user.getVillage() == null) {
            throw new IllegalArgumentException("User or user's village not found");
        }
        return getProvinceNameByVillageId(user.getVillage().getLocationId());
    }
}
