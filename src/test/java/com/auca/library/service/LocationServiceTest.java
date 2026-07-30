package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Location;
import com.auca.library.domain.enums.LocationType;
import com.auca.library.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocationServiceTest {
    private LocationDao locationDao;
    private UserDao userDao;
    private LocationService locationService;

    @Before
    public void setUp() {
        locationDao = mock(LocationDao.class);
        userDao = mock(UserDao.class);
        locationService = new LocationService(locationDao, userDao);
    }

    @Test
    public void createProvince_withNoParent_succeeds() {
        Location province = new Location();
        province.setLocationCode("KIGALI");
        province.setLocationType(LocationType.PROVINCE);
        
        when(locationDao.findByCode("KIGALI")).thenReturn(null);
        
        locationService.createLocation(province, null);
        verify(locationDao).save(province);
    }

    @Test
    public void createDistrict_withValidProvinceParent_succeeds() {
        Location province = new Location();
        province.setLocationType(LocationType.PROVINCE);
        UUID provId = UUID.randomUUID();
        
        Location district = new Location();
        district.setLocationCode("GASABO");
        district.setLocationType(LocationType.DISTRICT);
        
        when(locationDao.findByCode("GASABO")).thenReturn(null);
        when(locationDao.findById(provId)).thenReturn(province);
        
        locationService.createLocation(district, provId);
        verify(locationDao).save(district);
        assertEquals(province, district.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDistrict_withMissingParent_throwsException() {
        Location district = new Location();
        district.setLocationCode("GASABO");
        district.setLocationType(LocationType.DISTRICT);
        
        when(locationDao.findByCode("GASABO")).thenReturn(null);
        
        locationService.createLocation(district, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createLocation_duplicateLocationCode_throwsException() {
        Location province = new Location();
        province.setLocationCode("KIGALI");
        
        when(locationDao.findByCode("KIGALI")).thenReturn(new Location());
        
        locationService.createLocation(province, null);
    }

    @Test
    public void validVillageId_returnsCorrectProvinceName() {
        Location province = new Location();
        province.setLocationType(LocationType.PROVINCE);
        province.setLocationName("Kigali City");
        
        Location district = new Location();
        district.setLocationType(LocationType.DISTRICT);
        district.setParent(province);
        
        Location sector = new Location();
        sector.setLocationType(LocationType.SECTOR);
        sector.setParent(district);
        
        Location cell = new Location();
        cell.setLocationType(LocationType.CELL);
        cell.setParent(sector);
        
        Location village = new Location();
        village.setLocationType(LocationType.VILLAGE);
        village.setParent(cell);
        
        UUID villageId = village.getLocationId();
        
        when(locationDao.findById(villageId)).thenReturn(village);
        when(locationDao.findById(cell.getLocationId())).thenReturn(cell);
        when(locationDao.findById(sector.getLocationId())).thenReturn(sector);
        when(locationDao.findById(district.getLocationId())).thenReturn(district);
        when(locationDao.findById(province.getLocationId())).thenReturn(province);
        
        String provName = locationService.getProvinceNameByVillageId(villageId);
        assertEquals("Kigali City", provName);
    }

    @Test
    public void validPersonId_returnsCorrectProvinceName() {
        Location province = new Location();
        province.setLocationType(LocationType.PROVINCE);
        province.setLocationName("Kigali City");
        
        Location district = new Location();
        district.setLocationType(LocationType.DISTRICT);
        district.setParent(province);
        
        Location sector = new Location();
        sector.setLocationType(LocationType.SECTOR);
        sector.setParent(district);
        
        Location cell = new Location();
        cell.setLocationType(LocationType.CELL);
        cell.setParent(sector);
        
        Location village = new Location();
        village.setLocationType(LocationType.VILLAGE);
        village.setParent(cell);
        
        User user = new User();
        user.setVillage(village);
        UUID userId = user.getPersonId();
        
        when(userDao.findById(userId)).thenReturn(user);
        when(locationDao.findById(village.getLocationId())).thenReturn(village);
        when(locationDao.findById(cell.getLocationId())).thenReturn(cell);
        when(locationDao.findById(sector.getLocationId())).thenReturn(sector);
        when(locationDao.findById(district.getLocationId())).thenReturn(district);
        when(locationDao.findById(province.getLocationId())).thenReturn(province);
        
        String provName = locationService.getProvinceNameByPersonId(userId);
        assertEquals("Kigali City", provName);
    }
}
