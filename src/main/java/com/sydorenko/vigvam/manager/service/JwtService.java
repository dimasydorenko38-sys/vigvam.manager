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
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        String contracts = "";
        if(employee.getContractsEmployee() != null){
            contracts = employee.getContractsEmployee()
                    .stream()
                    .filter(contract -> contract.getStatus().equals(Status.ENABLED))
                    .map(contract -> contract.getRole().toString())
                    .collect(Collectors.joining(" "));
        }

        Map<String, Object> map = Map.of(
                "id",employee.getId(),
                "scope", contracts
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ваш профіль було деактивовано, австоризуйтеся повторно або зверніться до Адміністратора");
        }
        UserEntity user = repositories.stream()
                .map(repo -> repo.findByRefreshTokenAndStatus(dto.getRefreshToken(), Status.ENABLED))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Користувача деактивовано або не зареєстровано"));
        return switch (user) {
            case ClientEntity client -> generateToken(client);
            case EmployeeEntity employee -> generateToken(employee);
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported user type");
        };
    }

    public @Nullable AuthResponseDto loginUserByLogPass(UserLoginDto dto) {
        UserEntity user = repositories.stream()
                .map(repo -> repo.findByLoginAndPasswordAndStatus(dto.getLogin(), dto.getPassword(), Status.ENABLED))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Логін не знайденто, або хибний пароль"));
        UUID refreshToken = user.getRefreshToken();
        String token = switch (user){
            case ClientEntity client -> generateToken(client);
            case EmployeeEntity employee -> generateToken(employee);
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported user type");
        };
        return new AuthResponseDto(token,refreshToken);
    }
}
