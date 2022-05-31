package io.spring.jbuy.features.user;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.common.mapper.ReferenceMapper;
import io.spring.jbuy.features.authority.Authority;
import io.spring.jbuy.features.authority.AuthorityRepository;
import io.spring.jbuy.features.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static io.spring.jbuy.common.constant.Role.RoleName.CUSTOMER;

@Service
@RequiredArgsConstructor @Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ReferenceMapper referenceMapper;

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(User.class, userId));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(UUID userId) {
        return userMapper.toUserResponse(getUserById(userId));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUserResponsePageable(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest, boolean enabled) {
        return userMapper.toUserResponse(this.createInternalUser(userRequest, enabled));
    }

    @Transactional
    public User createInternalUser(UserRequest userRequest, boolean enabled) {
        if (isEmailAlreadyExisted(userRequest.getEmail())) {
            throw new ValidationException("Email is already registered!");
        }

        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEnabled(enabled);

        Authority role = authorityRepository.findByRole(CUSTOMER.getName()).orElse(null);
        user.setListOfAuthority(new HashSet<>());
        user.getListOfAuthority().add(role);

        return userRepository.save(user);
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        User currentUser = getUserById(userId);
        return userMapper.toUserResponse(userMapper.toExistingUser(userRequest, currentUser));
    }

    @Transactional
    public void deleteById(UUID userId) {
        if (userRepository.existsById(userId)) {
            // check if customer profile exist for current userId and delete it first,
            // admin profile and admin user should not be deleted (it will throw error on deletion attempt)
            if (customerRepository.existsById(userId)) {
                customerRepository.deleteById(userId);
            }
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException(User.class, userId);
        }
    }

    @Transactional
    public void setUserEnabled(UUID userId, boolean enabled) {
        User user = getUserById(userId);
        user.setEnabled(enabled);
    }

    @Transactional
    public void changeUserAuthorities(UUID userId, AuthorityChangeRequest authorityChangeRequest) {
        User user = getUserById(userId);
        user.setListOfAuthority(this.authorityChangeRequestToAuthoritySet(authorityChangeRequest));
    }

    @Transactional
    public void changeUserPassword(UUID userId, PasswordChangeRequest passwordChangeRequest) {
        User user = getUserById(userId);
        String currentPassword = passwordChangeRequest.getCurrentPassword();
        String newPassword = passwordChangeRequest.getNewPassword();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ValidationException("Incorrect current password!");
        }
        if (currentPassword.equals(newPassword)) {
            throw new ValidationException("New password must be different from current password!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional(readOnly = true)
    public boolean isUserEnabled(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() && user.get().isEnabled();
    }

    @Transactional(readOnly = true)
    public boolean isEmailAlreadyExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    private Set<Authority> authorityChangeRequestToAuthoritySet(AuthorityChangeRequest authorityChangeRequest) {
        Set<UUID> listOfAuthorityId = authorityChangeRequest.getListOfAuthorityId();
        if (listOfAuthorityId == null) {
            throw new ValidationException("Invalid authority change request!");
        }

        Set<Authority> authoritySet = new HashSet<>(Math.max((int) (listOfAuthorityId.size() / .75f) + 1, 16));
        for (UUID id : listOfAuthorityId) {
            authoritySet.add(referenceMapper.idToEntity(id, Authority.class));
        }

        return authoritySet;
    }
}
