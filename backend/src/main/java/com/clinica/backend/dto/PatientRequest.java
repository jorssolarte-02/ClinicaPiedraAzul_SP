package com.clinica.backend.dto;
import jakarta.validation.constraints.*; import java.time.LocalDate;
public record PatientRequest(@NotBlank @Size(min=5,max=20) String cedula,@NotBlank @Size(max=80) String firstName,@NotBlank @Size(max=80) String lastName,@NotNull @Past LocalDate birthDate,@Size(max=30) String phone,@NotBlank @Email String email,@Size(max=200) String address,@NotBlank @Size(min=8,max=100) String password){}
