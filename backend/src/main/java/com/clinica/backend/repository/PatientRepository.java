package com.clinica.backend.repository;
import com.clinica.backend.entity.Patient; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface PatientRepository extends JpaRepository<Patient,Long>{boolean existsByCedula(String cedula); Optional<Patient> findByUserId(Long userId);}
