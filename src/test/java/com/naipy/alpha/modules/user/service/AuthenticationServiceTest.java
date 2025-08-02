package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.core.security.jwt.JwtService;
import com.naipy.alpha.modules.token.Token;
import com.naipy.alpha.modules.token.TokenRepository;
import com.naipy.alpha.modules.user.models.AuthenticationRequest;
import com.naipy.alpha.modules.user.models.AuthenticationResponse;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticateAfterInsertingNewUser_ShouldSaveToken_WhenTokenIsGeneratedSuccessfully() {
        User user = new User();
        String jwtToken = "generatedToken";

        Mockito.when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthenticationResponse response = authenticationService.authenticateAfterInsertingNewUser(user);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        Mockito.verify(tokenRepository).save(Mockito.any(Token.class));
    }

    @Test
    void authenticate_ShouldThrowException_WhenAuthenticationFails() {
        AuthenticationRequest request = new AuthenticationRequest("user", "wrongPassword");

        Mockito.doThrow(new RuntimeException("Authentication failed"))
                .when(authManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
        Mockito.verify(tokenRepository, Mockito.never()).save(Mockito.any(Token.class));
    }

    @Test
    void revokeAllUserTokens_ShouldMarkTokensAsExpiredAndRevoked_WhenValidTokensExist() {
        User user = new User();
        List<Token> validTokens = List.of(new Token(), new Token());

        Mockito.when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validTokens);

        authenticationService.revokeAllUserTokens(user);

        validTokens.forEach(token -> {
            assertTrue(token.isExpired());
            assertTrue(token.isRevoked());
        });
        Mockito.verify(tokenRepository).saveAll(validTokens);
    }

    @Test
    void revokeAllUserTokens_ShouldDoNothing_WhenNoValidTokensExist() {
        User user = new User();

        Mockito.when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of());

        authenticationService.revokeAllUserTokens(user);

        Mockito.verify(tokenRepository, Mockito.never()).saveAll(Mockito.anyList());
    }
}