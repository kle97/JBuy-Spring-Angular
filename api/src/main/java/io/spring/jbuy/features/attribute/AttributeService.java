package io.spring.jbuy.features.attribute;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.validation.ValidationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @Transactional(readOnly = true)
    public Attribute getAttributeById(UUID attributeId) {
        return attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException(Attribute.class, attributeId));
    }

    @Transactional(readOnly = true)
    public AttributeResponse getAttributeResponseById(UUID attributeId) {
        return attributeMapper.toAttributeResponse(getAttributeById(attributeId));
    }

    @Transactional(readOnly = true)
    public Page<AttributeResponse> getAttributeResponsePageable(Pageable pageable) {
        return attributeRepository.findAll(pageable).map(attributeMapper::toAttributeResponse);
    }

    @Transactional
    public AttributeResponse createAttribute(AttributeRequest attributeRequest) {
        validateAttributeRequest(attributeRequest, null);
        Attribute transientAttribute = attributeMapper.toAttribute(attributeRequest);
        return attributeMapper.toAttributeResponse(attributeRepository.save(transientAttribute));
    }

    @Transactional
    public AttributeResponse updateAttribute(UUID attributeId, AttributeRequest attributeRequest) {
        Attribute currentAttribute = getAttributeById(attributeId);
        validateAttributeRequest(attributeRequest, currentAttribute);
        return attributeMapper.toAttributeResponse(
                attributeMapper.toExistingAttribute(attributeRequest, currentAttribute));
    }

    @Transactional
    public void deleteById(UUID attributeId) {
        if (attributeRepository.existsById(attributeId)) {
            attributeRepository.deleteById(attributeId);
        } else {
            throw new ResourceNotFoundException(Attribute.class, attributeId);
        }
    }

    private void validateAttributeRequest(AttributeRequest attributeRequest, @Nullable Attribute attribute) {
        if (attribute == null || (attributeRequest.getAttributeTypeId() != null &&
                !attributeRequest.getAttributeTypeId().equals(attribute.getAttributeType().getId()))) {
            if (attributeRepository.existsByAttributeType_IdAndName(attributeRequest.getAttributeTypeId(),
                                                                    attributeRequest.getName())) {
                throw new ValidationException("Attribute name already exists for attribute type!");
            }
        }
    }
}
