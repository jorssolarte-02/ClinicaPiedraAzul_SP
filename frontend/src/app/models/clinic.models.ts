export type Role = 'ADMIN' | 'PATIENT';
export interface LoginResponse { token: string; role: Role; email: string; }
export interface Doctor { id: number; cedula: string; firstName: string; lastName: string; specialty: string; phone: string; email: string; }
export interface Patient { id: number; cedula: string; firstName: string; lastName: string; birthDate: string; phone: string; email: string; address: string; }
export interface Appointment { id: number; patientId: number; patientName: string; doctorId: number; doctorName: string; specialty: string; date: string; time: string; reason: string; status: string; }
export interface PublicPatient { label: string; }
