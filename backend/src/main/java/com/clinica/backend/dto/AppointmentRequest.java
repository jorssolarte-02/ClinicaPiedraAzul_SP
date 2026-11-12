package com.clinica.backend.dto;
import jakarta.validation.constraints.*; import java.time.LocalDate; import java.time.LocalTime;
public record AppointmentRequest(@NotNull Long patientId,@NotNull Long doctorId,@NotNull @FutureOrPresent LocalDate date,@NotNull LocalTime time,@NotBlank @Size(max=500) String reason,@NotBlank String status){}
