package com.example.authservice.service.impl;

import com.example.authservice.component.JwtComponent;
import com.example.authservice.dto.AuthenticationRequest;
import com.example.authservice.entity.Token;
import com.example.authservice.entity.User;
import com.example.authservice.repo.TokenRepository;
import com.example.authservice.repo.UserRepository;
import com.example.authservice.utils.TokenType;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtComponent jwtComponent;

    private final AuthenticationManager authenticationManager;

    public Map<String, Object> authenticate(AuthenticationRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Optional<User> userOptional = repository.findByUsername(request.getUsername());
            User user = userOptional.get();
            String jwtToken = jwtComponent.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            map.put("access_token", jwtToken);
            map.put("status", true);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            addToMap(map);
            return map;
        }
    }

    public boolean validateToken(String toke) {
        return tokenRepository.findByToken(toke).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
    }

    private void addToMap(Map<String, Object> map) {
        map.put("msg", "Đăng nhập thất bại");
        map.put("status", false);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUserId(user.getId());
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        tokenRepository.deleteTokenById(user.getId());
    }
}
