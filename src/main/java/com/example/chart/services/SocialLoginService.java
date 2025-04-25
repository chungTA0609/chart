package com.example.chart.services;

import com.example.chart.models.Role;
import com.example.chart.models.RoleName;
import com.example.chart.models.User;
import com.example.chart.repository.RoleRepository;
import com.example.chart.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class SocialLoginService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SocialLoginService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);

                    Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                            .orElseThrow(() -> new RuntimeException("Customer role not found"));

                    newUser.setRoles(new HashSet<>() {{ add(customerRole); }});
                    return userRepository.save(newUser);
                });

        return oAuth2User;
    }
}
