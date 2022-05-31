package io.spring.jbuy.features.authority;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.UUID;

@Service
@Slf4j @RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    @Transactional(readOnly = true)
    public Authority getAuthorityById(UUID authorityId) {
        return authorityRepository.findById(authorityId)
                .orElseThrow(() -> new ResourceNotFoundException(Authority.class, authorityId));
    }

    @Transactional(readOnly = true)
    public AuthorityResponse getAuthorityResponseById(UUID authorityId) {
        return authorityMapper.toAuthorityResponse(getAuthorityById(authorityId));
    }

    @Transactional(readOnly = true)
    public Page<AuthorityResponse> getAuthorityResponsePageable(Pageable pageable) {
        return authorityRepository.findAll(pageable).map(authorityMapper::toAuthorityResponse);
    }

    @Transactional
    public AuthorityResponse createAuthority(AuthorityRequest authorityRequest) {
        validateAuthorityRequest(authorityRequest);
        Authority transientAuthority = authorityMapper.toAuthority(authorityRequest);
        return authorityMapper.toAuthorityResponse(authorityRepository.save(transientAuthority));
    }

    @Transactional
    public AuthorityResponse updateAuthority(UUID authorityId, AuthorityRequest authorityRequest) {
        Authority currentAuthority = getAuthorityById(authorityId);
        validateAuthorityRequest(authorityRequest);
        return authorityMapper.toAuthorityResponse(
                authorityMapper.toExistingAuthority(authorityRequest, currentAuthority));
    }

    @Transactional
    public void deleteById(UUID authorityId) {
        if (authorityRepository.existsById(authorityId)) {
            authorityRepository.deleteById(authorityId);
        } else {
            throw new ResourceNotFoundException(Authority.class, authorityId);
        }
    }

    private void validateAuthorityRequest(AuthorityRequest authorityRequest) {
        if (authorityRepository.existsByRole(authorityRequest.getRole())) {
            throw new ValidationException("Authority Role already exists!");
        }
    }
}
