package com.clinica.backend.repository;
import com.clinica.backend.entity.Appointment; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{List<Appointment> findAllByOrderByDateAscTimeAsc(); List<Appointment> findByPatientUserIdOrderByDateAscTimeAsc(Long userId);}
