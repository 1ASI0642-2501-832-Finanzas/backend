package me.ryzeon.finanzas.service.impl;

import lombok.AllArgsConstructor;
import me.ryzeon.authservice.dto.AuthRequest;
import me.ryzeon.authservice.dto.AuthResponse;
import me.ryzeon.authservice.dto.RegisterRequest;
import me.ryzeon.authservice.entity.Role;
import me.ryzeon.authservice.entity.User;
import me.ryzeon.authservice.repository.UserRepository;
import me.ryzeon.authservice.service.AuthService;
import me.ryzeon.authservice.service.JwtService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService, ApplicationContextAware {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private AuthService getSelf() {
        return applicationContext.getBean(AuthService.class);
    }


    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public void createUserFromSocio(String dni, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setUsername(dni);

        getSelf().register(registerRequest);
    }

    @Override
    public void deleteUser(String dni) {
        userRepository.findByUsername(dni).ifPresent(userRepository::delete);
    }
}