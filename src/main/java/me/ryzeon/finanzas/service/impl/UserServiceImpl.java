package me.ryzeon.finanzas.service.impl;

import lombok.RequiredArgsConstructor;
import me.ryzeon.finanzas.entity.User;
import me.ryzeon.finanzas.repository.UserRepository;
import me.ryzeon.finanzas.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:27
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByIdentifier(String identifier) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userRepository.findByUsername(identifier);
        }
        if (user.isEmpty()) {
            try {
                Long id = Long.parseLong(identifier);
                user = userRepository.findById(id);
            } catch (NumberFormatException ignored) {
            }
        }
        return user;
    }
}
