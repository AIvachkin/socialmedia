package com.github.aivachkin.socialmedia.controller;

import com.github.aivachkin.socialmedia.domain.JwtAuthentication;
import com.github.aivachkin.socialmedia.domain.JwtUser;
import com.github.aivachkin.socialmedia.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для вывода приветственных сообщений пользователям в зависимости от их ролей (тестовый)
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

    private final AuthService authService;

//    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/hello/user")
    public ResponseEntity<String> helloUser () {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        JwtUser jwtUser = (JwtUser) authInfo.getPrincipal();

        return ResponseEntity.ok("Hello user " + jwtUser.getUsername() + "!");
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        JwtUser jwtUser = (JwtUser) authInfo.getPrincipal();

        return ResponseEntity.ok("Hello admin " + jwtUser.getUsername() + "!");
    }
}
