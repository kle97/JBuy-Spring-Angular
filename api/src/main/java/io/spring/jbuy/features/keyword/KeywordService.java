package io.spring.jbuy.features.keyword;

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
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final KeywordMapper keywordMapper;

    @Transactional(readOnly = true)
    public Keyword getKeywordById(UUID keywordId) {
        return keywordRepository.findById(keywordId)
                .orElseThrow(() -> new ResourceNotFoundException(Keyword.class, keywordId));
    }

    @Transactional(readOnly = true)
    public KeywordResponse getKeywordResponseById(UUID keywordId) {
        return keywordMapper.toKeywordResponse(getKeywordById(keywordId));
    }

    @Transactional(readOnly = true)
    public Page<KeywordResponse> getKeywordResponsePageable(Pageable pageable) {
        return keywordRepository.findAll(pageable).map(keywordMapper::toKeywordResponse);
    }

    @Transactional
    public KeywordResponse createKeyword(KeywordRequest keywordRequest) {
        if (keywordRepository.existsByName(keywordRequest.getName())) {
            throw new ValidationException("keyword already exists!");
        }
        Keyword transientKeyword = keywordMapper.toKeyword(keywordRequest);
        transientKeyword.setName(keywordRequest.getName().toLowerCase());
        return keywordMapper.toKeywordResponse(keywordRepository.save(transientKeyword));
    }

    @Transactional
    public KeywordResponse updateKeyword(UUID keywordId, KeywordRequest keywordRequest) {
        Keyword currentKeyword = getKeywordById(keywordId);
        return keywordMapper.toKeywordResponse(keywordMapper.toExistingKeyword(keywordRequest, currentKeyword));
    }

    @Transactional
    public void deleteById(UUID keywordId) {
        if (keywordRepository.existsById(keywordId)) {
            keywordRepository.deleteById(keywordId);
        } else {
            throw new ResourceNotFoundException(Keyword.class, keywordId);
        }
    }
}
