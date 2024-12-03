package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.core.security.jwt.JwtService;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.AuthenticationRequest;
import com.naipy.alpha.modules.user.models.AuthenticationResponse;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.token.Token;
import com.naipy.alpha.modules.token.TokenRepository;
import com.naipy.alpha.modules.token.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse authenticateAfterInsertingNewUser (User user) {
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
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
        User user = userRepository.findByEmail(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens (User user) {
        List<Token> validUserTokenList = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokenList.isEmpty())
            return;

        validUserTokenList.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokenList);
    }

    private void saveUserToken (User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }
}
