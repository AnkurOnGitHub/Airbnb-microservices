package com.airbnb.user_service.controllers;

import com.airbnb.user_service.dtos.request.RefreshTokenRequest;
import com.airbnb.user_service.dtos.response.TokenResponse;
import com.airbnb.user_service.exceptions.ApiException;
import com.airbnb.user_service.payloads.ApiResponse;
import com.airbnb.user_service.services.auth.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthTokenService authTokenService;
    private final Map<String,String>AUTH_PROVIDERS =Map.of(
            "google", "/oauth2/authorization/google",
            "github", "/oauth2/authorization/github"

    );
    @GetMapping("/login-uri/{provider}")
    public RedirectView login(@PathVariable String provider){
        String providerUrl = AUTH_PROVIDERS.get(provider);
        if(providerUrl == null)
            throw new ApiException("Invalid provider "+ provider, HttpStatus.BAD_REQUEST);
        else
            return new RedirectView(providerUrl);
    }

    @GetMapping("/get-tokens")
    public ResponseEntity<ApiResponse<TokenResponse>>getToken(Authentication authentication){
        final TokenResponse tokenResponse = authTokenService.issueToken(authentication);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse,"Authenticated"));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>>refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(
                ApiResponse.success(
                        authTokenService.refreshToken(refreshTokenRequest),
                        "Tokens refreshed"
                )
        );
    }
    @GetMapping("/error")
    public ResponseEntity<ApiResponse<Void>>error(){
        ApiResponse<Void>apiResponse = ApiResponse.error("Authentication failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }


}
