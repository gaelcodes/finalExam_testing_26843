package com.auca.library.domain;

import com.auca.library.domain.enums.Gender;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Person {
    @Id
    private UUID personId = UUID.randomUUID();
    private String firstName;
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String phoneNumber;
}
