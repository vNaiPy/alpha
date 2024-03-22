package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.core.security.jwt.JwtService;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.AuthenticationRequest;
import com.naipy.alpha.modules.user.models.AuthenticationResponse;
import com.naipy.alpha.modules.user.models.RegisterRequest;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.token.Token;
import com.naipy.alpha.modules.token.TokenRepository;
import com.naipy.alpha.modules.token.TokenType;
import com.naipy.alpha.modules.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserRepository _userRepository;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authManager;

    @Autowired
    private final TokenRepository _tokenRepository;

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
        User user = _userRepository.findByEmail(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
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
