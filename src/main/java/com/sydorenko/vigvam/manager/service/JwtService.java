package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.RefreshTokenDto;
import com.sydorenko.vigvam.manager.dto.request.UserLoginDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import com.sydorenko.vigvam.manager.persistence.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {

    private final List<UserRepository<? extends UserEntity>> repositories;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Long id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(ClientEntity client) {
        Map<String, Object> map = Map.of(
                "id",client.getId(),
                "scope",client.getRole()
        );
        return Jwts.builder()
                .claims(map)
                .subject(String.valueOf(client.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(EmployeeEntity employee) {
        Map<String, Object> map = Map.of(
                "id",employee.getId(),
                "scope",employee.getContractsEmployee()
                        .stream()
                        .filter(contract -> contract.getStatus().equals(Status.ENABLED))
                        .map(contract -> contract.getRole().toString())
                        .collect(Collectors.joining(" "))
        );
        return Jwts.builder()
                .claims(map)
                .subject(String.valueOf(employee.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }


    public String generateTokenByRefreshToken(RefreshTokenDto dto) {
        if(dto.getRefreshToken() == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "RefreshToken can not be null");
        }
        UserEntity user = repositories.stream()
                .map(repo -> repo.findByRefreshToken(dto.getRefreshToken()))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token not found"));
        System.out.println(user);
        return switch (user) {
            case ClientEntity client -> generateToken(client);
            case EmployeeEntity employee -> generateToken(employee);
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported user type");
        };
    }

    public @Nullable AuthResponseDto loginUserByLogPass(UserLoginDto dto) {
        UserEntity user = repositories.stream()
                .map(repo -> repo.findByLogin(dto.getLogin()))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login not found"));
        if(user.getPassword() == null || !user.getPassword().equals(dto.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is not correct");
        }
        UUID refreshToken = user.getRefreshToken();
        String token = switch (user){
            case ClientEntity client -> generateToken(client);
            case EmployeeEntity employee -> generateToken(employee);
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported user type");
        };
        return new AuthResponseDto(token,refreshToken);
    }
}
