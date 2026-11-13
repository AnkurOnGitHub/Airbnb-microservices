package com.airbnb.user_service.services.impl;

import com.airbnb.user_service.entities.User;
import com.airbnb.user_service.repositories.UserRepository;
import com.airbnb.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User createOrUpdateUser(String provider, OAuth2User authUser) {
        String providerSubject = authUser.getName();
         Optional<User> optionalUser = userRepository.findByProviderAndProviderSubject(
                provider,
                 authUser.getAttribute(providerSubject)
        );

         if(optionalUser.isPresent()){
             User user = optionalUser.get();
             user.setEmail(authUser.getAttribute("email"));
             user.setName(authUser.getAttribute("name"));
             return user;
         }
         User user = User.builder()
                 .email(authUser.getAttribute("email"))
                 .name(authUser.getAttribute("name"))
                 .provider(provider)
                 .providerSubject(providerSubject)
                 .build();
         return userRepository.save(user);
    }
}
