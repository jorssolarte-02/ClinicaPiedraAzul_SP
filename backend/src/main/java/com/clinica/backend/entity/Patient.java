package com.clinica.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name="patients", uniqueConstraints=@UniqueConstraint(columnNames="cedula"))
public class Patient {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=20) private String cedula;
    @Column(nullable=false) private String firstName;
    @Column(nullable=false) private String lastName;
    @Column(nullable=false) private LocalDate birthDate;
    private String phone;
    @Column(nullable=false) private String email;
    private String address;
    @OneToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false, unique=true) private UserAccount user;
    protected Patient() {}
    public Patient(String cedula,String firstName,String lastName,LocalDate birthDate,String phone,String email,String address,UserAccount user){this.cedula=cedula;this.firstName=firstName;this.lastName=lastName;this.birthDate=birthDate;this.phone=phone;this.email=email.toLowerCase().trim();this.address=address;this.user=user;}
    public Long getId(){return id;} public String getCedula(){return cedula;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public LocalDate getBirthDate(){return birthDate;} public String getPhone(){return phone;} public String getEmail(){return email;} public String getAddress(){return address;} public UserAccount getUser(){return user;}
}
