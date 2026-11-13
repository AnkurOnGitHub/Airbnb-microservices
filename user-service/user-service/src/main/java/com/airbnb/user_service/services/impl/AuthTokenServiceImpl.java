package com.airbnb.user_service.services.impl;
import com.airbnb.user_service.dtos.request.RefreshTokenRequest;
import com.airbnb.user_service.dtos.response.TokenResponse;
import com.airbnb.user_service.entities.User;
import com.airbnb.user_service.exceptions.ApiException;
import com.airbnb.user_service.exceptions.ResourceNotExistException;
import com.airbnb.user_service.repositories.UserRepository;
import com.airbnb.user_service.services.auth.AuthTokenService;
import com.airbnb.user_service.utilities.JWTUtility;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JWTUtility jwtUtility;
    private final UserRepository userRepository;

    @Override
    public TokenResponse issueToken(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken authenticationToken)){
            throw new ApiException("OAuth Login required", HttpStatus.BAD_REQUEST);
        }
        String provider = authenticationToken.getAuthorizedClientRegistrationId();
        String subject = authentication.getName();
        User user = userRepository.findByProviderAndProviderSubject(provider, subject)
                .orElseThrow(()->new ResourceNotExistException("User not found"));

        return new TokenResponse(jwtUtility.generateAccessToken(user),jwtUtility.generateRefreshToken(user));
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        Claims userClaims = jwtUtility.parseRefreshToken((refreshToken));
        Long userId = Long.valueOf(userClaims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotExistException("User does not exist"));
        return new TokenResponse(
                jwtUtility.generateAccessToken(user),
                jwtUtility.generateRefreshToken(user)
        );
    }
}
