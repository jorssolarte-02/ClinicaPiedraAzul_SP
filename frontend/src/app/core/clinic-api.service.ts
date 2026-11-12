import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, Doctor, Patient, PublicPatient } from '../models/clinic.models';

@Injectable({ providedIn: 'root' })
export class ClinicApiService {
  private readonly base = 'http://localhost:8080/api';
  constructor(private http: HttpClient) {}
  doctors(publicOnly = false): Observable<Doctor[]> { return this.http.get<Doctor[]>(`${this.base}/doctors${publicOnly ? '/public' : ''}`); }
  patients(): Observable<Patient[]> { return this.http.get<Patient[]>(`${this.base}/patients`); }
  publicPatients(): Observable<PublicPatient[]> { return this.http.get<PublicPatient[]>(`${this.base}/patients/public`); }
  patientExists(cedula: string): Observable<{ exists: boolean }> { return this.http.get<{ exists: boolean }>(`${this.base}/patients/exists/${encodeURIComponent(cedula)}`); }
  createDoctor(payload: object): Observable<Doctor> { return this.http.post<Doctor>(`${this.base}/doctors`, payload); }
  createPatient(payload: object): Observable<Patient> { return this.http.post<Patient>(`${this.base}/patients`, payload); }
  appointments(mine = false): Observable<Appointment[]> { return this.http.get<Appointment[]>(`${this.base}/appointments${mine ? '/my' : ''}`); }
  createAppointment(payload: object): Observable<Appointment> { return this.http.post<Appointment>(`${this.base}/appointments`, payload); }
}
