import { Routes } from '@angular/router';
import { ClinicLayoutComponent } from './templates/clinic-layout.component';
import { TablePageComponent } from './pages/table-page.component';
import { LoginPageComponent } from './pages/login-page.component';
import { DashboardPageComponent } from './pages/dashboard-page.component';
import { DoctorFormPageComponent } from './pages/doctor-form-page.component';
import { PatientFormPageComponent } from './pages/patient-form-page.component';
import { AppointmentFormPageComponent } from './pages/appointment-form-page.component';
import { ForbiddenPageComponent } from './pages/forbidden-page.component';
import { authenticationGuard, roleGuard } from './core/auth.guards';

const doctorColumns=[{key:'firstName',label:'Nombre'},{key:'lastName',label:'Apellido'},{key:'specialty',label:'Especialidad'},{key:'cedula',label:'Identificación'},{key:'email',label:'Correo'}];
const patientColumns=[{key:'firstName',label:'Nombre'},{key:'lastName',label:'Apellido'},{key:'cedula',label:'Identificación'},{key:'email',label:'Correo'},{key:'phone',label:'Teléfono'}];
const appointmentColumns=[{key:'patientName',label:'Paciente'},{key:'doctorName',label:'Médico'},{key:'specialty',label:'Especialidad'},{key:'date',label:'Fecha'},{key:'time',label:'Hora'},{key:'reason',label:'Motivo'},{key:'status',label:'Estado'}];
const myAppointmentColumns=[{key:'date',label:'Fecha'},{key:'time',label:'Hora'},{key:'doctorName',label:'Médico'},{key:'specialty',label:'Especialidad'},{key:'reason',label:'Motivo'},{key:'status',label:'Estado'}];

export const routes: Routes = [
 {path:'',component:ClinicLayoutComponent,children:[
  {path:'',pathMatch:'full',redirectTo:'doctors'},
  {path:'doctors',component:TablePageComponent,data:{kind:'doctorPublic',title:'Directorio médico',subtitle:'Conoce a los profesionales que cuidan de ti.',columns:doctorColumns}},
  {path:'login',component:LoginPageComponent},
  {path:'forbidden',component:ForbiddenPageComponent}
 ]},
 {path:'admin',component:ClinicLayoutComponent,canActivate:[authenticationGuard,roleGuard],data:{role:'ADMIN'},children:[
  {path:'',component:DashboardPageComponent},
  {path:'doctors/new',component:DoctorFormPageComponent},
  {path:'doctors',component:TablePageComponent,data:{kind:'doctors',title:'Equipo médico',subtitle:'Directorio administrativo de profesionales.',columns:doctorColumns}},
  {path:'patients/new',component:PatientFormPageComponent},
  {path:'patients',component:TablePageComponent,data:{kind:'patients',title:'Pacientes',subtitle:'Registros de pacientes de la clínica.',columns:patientColumns}},
  {path:'appointments/new',component:AppointmentFormPageComponent},
  {path:'appointments',component:TablePageComponent,data:{kind:'appointments',title:'Agenda de citas',subtitle:'Consultas programadas para todos los pacientes.',columns:appointmentColumns}}
 ]},
 {path:'patient',component:ClinicLayoutComponent,canActivate:[authenticationGuard,roleGuard],data:{role:'PATIENT'},children:[
  {path:'',pathMatch:'full',redirectTo:'appointments'},
  {path:'appointments',component:TablePageComponent,data:{kind:'myAppointments',title:'Mis citas',subtitle:'Tu agenda médica personal.',columns:myAppointmentColumns}}
 ]},
 {path:'**',redirectTo:'doctors'}
];
