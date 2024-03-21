package com.example.authservice.controller;

import com.example.authservice.dto.AuthenticationRequest;
import com.example.authservice.service.impl.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class LoinController {

    private final AuthenticationService authenticationService;

    public LoinController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/singin")
    public Map<String, Object> singIn(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping(value = "/validate")
    public boolean validateToken(@RequestParam String token) {
        return authenticationService.validateToken(token);
    }
}
