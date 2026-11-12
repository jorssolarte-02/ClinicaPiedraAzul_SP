package com.clinica.backend.entity;

import jakarta.persistence.*;

@Entity @Table(name="doctors", uniqueConstraints=@UniqueConstraint(columnNames="cedula"))
public class Doctor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=20) private String cedula;
    @Column(nullable=false) private String firstName;
    @Column(nullable=false) private String lastName;
    @Column(nullable=false) private String specialty;
    private String phone;
    @Column(nullable=false) private String email;
    protected Doctor() {}
    public Doctor(String cedula,String firstName,String lastName,String specialty,String phone,String email){this.cedula=cedula;this.firstName=firstName;this.lastName=lastName;this.specialty=specialty;this.phone=phone;this.email=email;}
    public Long getId(){return id;} public String getCedula(){return cedula;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getSpecialty(){return specialty;} public String getPhone(){return phone;} public String getEmail(){return email;}
}
