package com.example.todo_app.module.User.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;
import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserRepositories userRepositories;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, UserRepositories userRepositories) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.userRepositories = userRepositories;
    }

    public record LoginRequest(String username, String password) {
    }
    public record ResponseToken(String token) {
    }

    @PostMapping("/login")
    public ResponseToken login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            User user = userRepositories.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .orElse("USER");

            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(authentication.getName())
                    .claim("userId", user.getId())
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .claim("role", role)
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
            return new ResponseToken(token);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid username or password");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }
}
