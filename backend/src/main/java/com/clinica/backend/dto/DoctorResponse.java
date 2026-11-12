package com.clinica.backend.dto;
import com.clinica.backend.entity.Doctor;
public record DoctorResponse(Long id,String cedula,String firstName,String lastName,String specialty,String phone,String email){public static DoctorResponse from(Doctor d){return new DoctorResponse(d.getId(),d.getCedula(),d.getFirstName(),d.getLastName(),d.getSpecialty(),d.getPhone(),d.getEmail());}}
