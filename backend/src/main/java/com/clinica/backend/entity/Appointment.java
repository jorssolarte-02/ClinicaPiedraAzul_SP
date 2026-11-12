package com.clinica.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity @Table(name="appointments")
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="patient_id",nullable=false) private Patient patient;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="doctor_id",nullable=false) private Doctor doctor;
    @Column(name="appointment_date",nullable=false) private LocalDate date;
    @Column(name="appointment_time",nullable=false) private LocalTime time;
    @Column(nullable=false,length=500) private String reason;
    @Column(nullable=false) private String status;
    protected Appointment() {}
    public Appointment(Patient patient,Doctor doctor,LocalDate date,LocalTime time,String reason,String status){this.patient=patient;this.doctor=doctor;this.date=date;this.time=time;this.reason=reason;this.status=status;}
    public Long getId(){return id;} public Patient getPatient(){return patient;} public Doctor getDoctor(){return doctor;} public LocalDate getDate(){return date;} public LocalTime getTime(){return time;} public String getReason(){return reason;} public String getStatus(){return status;}
}
