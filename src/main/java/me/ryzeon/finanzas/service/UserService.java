package me.ryzeon.finanzas.service;

import me.ryzeon.finanzas.entity.User;

import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:26
 */
public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdentifier(String identifier);
}
