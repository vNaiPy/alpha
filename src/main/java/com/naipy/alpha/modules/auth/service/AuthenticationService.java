package com.naipy.alpha.modules.auth.service;

import com.naipy.alpha.core.security.jwt.JwtService;
import com.naipy.alpha.modules.auth.models.User;
import com.naipy.alpha.modules.auth.models.Role;
import com.naipy.alpha.modules.auth.enums.UserStatus;
import com.naipy.alpha.modules.auth.models.AuthenticationRequest;
import com.naipy.alpha.modules.auth.models.AuthenticationResponse;
import com.naipy.alpha.modules.auth.models.RegisterRequest;
import com.naipy.alpha.modules.auth.repository.UserRepository;
import com.naipy.alpha.modules.token.Token;
import com.naipy.alpha.modules.token.TokenRepository;
import com.naipy.alpha.modules.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserRepository _userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authManager;

    @Autowired
    private final TokenRepository _tokenRepository;

    public AuthenticationResponse register (RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();
        var savedUser = _userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
        );
        var user = _userRepository.findByEmail(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens (User user) {
        List<Token> validUserTokenList = _tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokenList.isEmpty())
            return;

        validUserTokenList.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        _tokenRepository.saveAll(validUserTokenList);
    }

    private void saveUserToken (User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        _tokenRepository.save(token);
    }
}
