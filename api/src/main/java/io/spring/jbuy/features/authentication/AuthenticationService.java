package io.spring.jbuy.features.authentication;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.user.User;
import io.spring.jbuy.features.user.UserMapper;
import io.spring.jbuy.features.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor @Slf4j
public class AuthenticationService {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserMapper userMapper;

    public UserResponse login() {
        User user = customUserDetailsService.getCurrentUser().orElseThrow(
                () -> new ResourceNotFoundException("Please provide credentials to login!"));
        return userMapper.toUserResponse(user);
    }
}
