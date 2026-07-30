package com.auca.library.domain;

import com.auca.library.domain.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends Person {
    private String username;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "villageId")
    private Location village;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Location getVillage() { return village; }
    public void setVillage(Location village) { this.village = village; }
}
