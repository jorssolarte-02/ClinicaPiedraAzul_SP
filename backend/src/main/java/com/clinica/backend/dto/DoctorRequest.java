package com.clinica.backend.dto;
import jakarta.validation.constraints.*;
public record DoctorRequest(@NotBlank @Size(min=5,max=20) String cedula,@NotBlank @Size(max=80) String firstName,@NotBlank @Size(max=80) String lastName,@NotBlank @Size(max=100) String specialty,@Size(max=30) String phone,@NotBlank @Email String email){}
