
package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.UserDto;
import me.ryzeon.finanzas.entity.User;
import me.ryzeon.finanzas.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/account")
@Tag(name = "Account", description = "Endpoints para la autenticación de usuarios")
@AllArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping("{identifier}")
    public HttpEntity<UserDto> getUser(@PathVariable String identifier) {
        Optional<User> user = userService.findByIdentifier(identifier);
        return user.map(value -> ResponseEntity.ok(UserDto.fromUser(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}