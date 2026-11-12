package com.clinica.backend.controller;
import com.clinica.backend.dto.*; import com.clinica.backend.service.ClinicService; import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController{private final ClinicService clinic;public AuthController(ClinicService c){clinic=c;}@PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest r){return clinic.login(r);}}
