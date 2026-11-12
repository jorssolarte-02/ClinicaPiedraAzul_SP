package com.clinica.backend.config;

import com.clinica.backend.entity.*; import com.clinica.backend.repository.*; import java.time.*; import org.springframework.boot.CommandLineRunner; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Component; import org.springframework.transaction.annotation.Transactional;

@Component
public class SeedData implements CommandLineRunner {
 private final UserRepository users; private final DoctorRepository doctors; private final PatientRepository patients; private final AppointmentRepository appointments; private final PasswordEncoder encoder;
 public SeedData(UserRepository u,DoctorRepository d,PatientRepository p,AppointmentRepository a,PasswordEncoder e){users=u;doctors=d;patients=p;appointments=a;encoder=e;}
 @Override @Transactional public void run(String... args){
  users.findByEmailIgnoreCase("admin@piedraazul.test").orElseGet(()->users.save(new UserAccount("admin@piedraazul.test",encoder.encode("Admin123!"),Role.ADMIN)));
  if(doctors.count()==0){doctors.save(new Doctor("100000001","Laura","Méndez","Medicina general","3001112233","laura.mendez@piedraazul.test"));doctors.save(new Doctor("100000002","Andrés","Rojas","Cardiología","3001112244","andres.rojas@piedraazul.test"));doctors.save(new Doctor("100000003","Camila","Vargas","Pediatría","3001112255","camila.vargas@piedraazul.test"));}
  ensurePatient("1020304050","Sofía","Ramírez","paciente@piedraazul.test","Paciente123!");
  ensurePatient("1020304051","Mateo","Torres","mateo@piedraazul.test","Paciente123!");
  if(appointments.count()==0&&patients.count()>0&&doctors.count()>0){var p=patients.findAll();var d=doctors.findAll();appointments.save(new Appointment(p.get(0),d.get(0),LocalDate.now().plusDays(2),LocalTime.of(9,0),"Control general","Programada"));appointments.save(new Appointment(p.get(0),d.get(1),LocalDate.now().plusDays(7),LocalTime.of(10,30),"Valoración cardiológica","Programada"));if(p.size()>1)appointments.save(new Appointment(p.get(1),d.get(2),LocalDate.now().plusDays(3),LocalTime.of(11,0),"Consulta de seguimiento","Programada"));}
 }
 private void ensurePatient(String cedula,String first,String last,String email,String password){if(patients.existsByCedula(cedula))return;UserAccount user=users.findByEmailIgnoreCase(email).orElseGet(()->users.save(new UserAccount(email,encoder.encode(password),Role.PATIENT)));patients.save(new Patient(cedula,first,last,LocalDate.of(1998,5,12),"3001234567",email,"Popayán",user));}
}
