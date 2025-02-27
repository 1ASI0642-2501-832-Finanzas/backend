package me.ryzeon.finanzas.dto;

import me.ryzeon.finanzas.entity.User;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:26
 */
public record UserDto(
        Long id,
        String email,
        String username
) {

    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername());
    }
}
