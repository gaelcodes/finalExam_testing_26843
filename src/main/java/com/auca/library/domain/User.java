package com.auca.library.domain;

import com.auca.library.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
}
