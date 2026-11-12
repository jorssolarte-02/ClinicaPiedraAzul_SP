package com.clinica.backend.dto;
import com.clinica.backend.entity.Patient; import java.time.LocalDate;
public record PatientResponse(Long id,String cedula,String firstName,String lastName,LocalDate birthDate,String phone,String email,String address){public static PatientResponse from(Patient p){return new PatientResponse(p.getId(),p.getCedula(),p.getFirstName(),p.getLastName(),p.getBirthDate(),p.getPhone(),p.getEmail(),p.getAddress());}}
