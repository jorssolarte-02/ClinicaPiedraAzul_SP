import { TestBed } from '@angular/core/testing';
import { provideRouter, Router, UrlTree } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';
import { App } from './app';
import { routes } from './app.routes';
import { AuthService } from './core/auth.service';
import { ClinicApiService } from './core/clinic-api.service';
import { authenticationGuard, roleGuard } from './core/auth.guards';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

describe('Application root', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({imports:[App]}).compileComponents();
  });
  it('creates the app and provides its route outlet', () => {
    const fixture=TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
    expect(fixture.nativeElement.querySelector('router-outlet')).toBeTruthy();
  });
});

describe('Authentication and role guards', () => {
  let session=false; let role:'ADMIN'|'PATIENT'|null=null; let router:Router;
  const route=(requiredRole?:'ADMIN'|'PATIENT')=>({data:requiredRole?{role:requiredRole}:{}}) as ActivatedRouteSnapshot;
  const state=(url:string)=>({url}) as RouterStateSnapshot;
  beforeEach(()=>{
    session=false;role=null;
    TestBed.configureTestingModule({providers:[provideRouter([]),{provide:AuthService,useValue:{isAuthenticated:()=>session,role:()=>role}}]});
    router=TestBed.inject(Router);
  });
  it('redirects a direct private URL to login when there is no session',()=>{
    const result=TestBed.runInInjectionContext(()=>authenticationGuard(route(),state('/admin/patients')));
    expect(result).toBeInstanceOf(UrlTree);
    expect(router.serializeUrl(result as UrlTree)).toContain('/login?returnUrl=%2Fadmin%2Fpatients');
  });
  it('allows an authenticated user through the authentication guard',()=>{
    session=true;role='ADMIN';
    expect(TestBed.runInInjectionContext(()=>authenticationGuard(route(),state('/admin')))).toBe(true);
  });
  it('allows the required role and redirects a missing session to login',()=>{
    session=true;role='PATIENT';
    expect(TestBed.runInInjectionContext(()=>roleGuard(route('PATIENT'),state('/patient/appointments')))).toBe(true);
    session=false;role=null;
    const result=TestBed.runInInjectionContext(()=>roleGuard(route('PATIENT'),state('/patient/appointments')));
    expect(result).toBeInstanceOf(UrlTree);expect(router.serializeUrl(result as UrlTree)).toBe('/login');
  });
  it('redirects an authenticated user with the wrong role to forbidden',()=>{
    session=true;role='PATIENT';
    const result=TestBed.runInInjectionContext(()=>roleGuard(route('ADMIN'),state('/admin/patients')));
    expect(result).toBeInstanceOf(UrlTree);expect(router.serializeUrl(result as UrlTree)).toBe('/forbidden');
  });
});

describe('Public and private route access by direct URL', () => {
  let signedIn=false;let role:'ADMIN'|'PATIENT'|null=null;let router:Router;
  beforeEach(()=>{
    signedIn=false;role=null;
    TestBed.configureTestingModule({providers:[
      provideRouter(routes),
      {provide:AuthService,useValue:{isAuthenticated:()=>signedIn,role:()=>role,email:()=>null}},
      {provide:ClinicApiService,useValue:{doctors:()=>of([]),patients:()=>of([]),publicPatients:()=>of([]),appointments:()=>of([]),patientExists:()=>of({exists:false}),createDoctor:()=>of({}),createPatient:()=>of({}),createAppointment:()=>of({})}}
    ]});
    router=TestBed.inject(Router);
  });
  it('opens the public doctor directory without a session',async()=>{
    const harness=await RouterTestingHarness.create();await harness.navigateByUrl('/doctors');
    expect(router.url).toBe('/doctors');expect(harness.routeNativeElement?.textContent).toContain('Directorio médico');
  });
  it('opens the public login page without a session',async()=>{
    const harness=await RouterTestingHarness.create();await harness.navigateByUrl('/login');
    expect(router.url).toBe('/login');expect(harness.routeNativeElement?.textContent).toContain('Iniciar sesión');
  });
  it('redirects direct private navigation to login when there is no session',async()=>{
    const harness=await RouterTestingHarness.create();await harness.navigateByUrl('/admin/patients');
    expect(router.url).toContain('/login');expect(harness.routeNativeElement?.textContent).toContain('ACCESO SEGURO');
  });
  it('allows ADMIN to open the administrator dashboard directly',async()=>{
    const harness=await RouterTestingHarness.create();signedIn=true;role='ADMIN';await harness.navigateByUrl('/admin');
    expect(router.url).toBe('/admin');expect(harness.routeNativeElement?.textContent).toContain('equipo Piedra Azul');
  });
  it('denies a direct administrator URL to PATIENT',async()=>{
    const harness=await RouterTestingHarness.create();signedIn=true;role='PATIENT';await harness.navigateByUrl('/admin/patients');
    expect(router.url).toBe('/forbidden');expect(harness.routeNativeElement?.textContent).toContain('no está disponible para tu rol');
  });
  it('allows PATIENT to open personal appointments directly',async()=>{
    const harness=await RouterTestingHarness.create();signedIn=true;role='PATIENT';await harness.navigateByUrl('/patient/appointments');
    expect(router.url).toBe('/patient/appointments');expect(harness.routeNativeElement?.textContent).toContain('Mis citas');
  });
  it('denies a direct patient URL to ADMIN',async()=>{
    const harness=await RouterTestingHarness.create();signedIn=true;role='ADMIN';await harness.navigateByUrl('/patient/appointments');
    expect(router.url).toBe('/forbidden');
  });
});
