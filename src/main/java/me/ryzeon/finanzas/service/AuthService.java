package me.ryzeon.finanzas.service;

import me.ryzeon.finanzas.dto.AuthRequest;
import me.ryzeon.finanzas.dto.AuthResponse;
import me.ryzeon.finanzas.dto.RegisterRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional
    AuthResponse register(RegisterRequest request);

    @Transactional
    AuthResponse authenticate(AuthRequest request);

    void createUserFromSocio(String dni, String email, String password);

    @Transactional
    void deleteUser(String dni);
}