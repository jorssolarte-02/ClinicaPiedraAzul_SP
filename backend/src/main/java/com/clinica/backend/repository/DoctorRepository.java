package com.clinica.backend.repository;
import com.clinica.backend.entity.Doctor; import org.springframework.data.jpa.repository.JpaRepository;
public interface DoctorRepository extends JpaRepository<Doctor,Long>{boolean existsByCedula(String cedula);}
