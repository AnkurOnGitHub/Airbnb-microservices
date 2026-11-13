package com.airbnb.user_service.services.auth;

import com.airbnb.user_service.dtos.response.TokenResponse;
import com.airbnb.user_service.dtos.request.RefreshTokenRequest;

import org.springframework.security.core.Authentication;

public interface AuthTokenService {
    TokenResponse issueToken(Authentication authentication);
    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
