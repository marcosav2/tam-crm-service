package com.gmail.marcosav2010.crm.api.controller;

import com.gmail.marcosav2010.crm.api.dto.AuthTokenDTO;
import com.gmail.marcosav2010.crm.api.dto.CredentialsDTO;
import com.gmail.marcosav2010.crm.api.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthenticationManager authenticationManager;

  private final JWTProvider jwtProvider;

  @Override
  public ResponseEntity<AuthTokenDTO> login(CredentialsDTO credentialsDTO) {
    final Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentialsDTO.getUsername(), credentialsDTO.getPassword()));

    final var token = jwtProvider.generateToken((UserDetails) authentication.getPrincipal());

    return ResponseEntity.ok(
        new AuthTokenDTO()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresIn(jwtProvider.getJwtExpiration()));
  }
}
