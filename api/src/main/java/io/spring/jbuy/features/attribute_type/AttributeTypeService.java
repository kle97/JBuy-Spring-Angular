package io.spring.jbuy.features.attribute_type;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j @RequiredArgsConstructor
public class AttributeTypeService {

    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeMapper attributeTypeMapper;

    @Transactional(readOnly = true)
    public AttributeType getAttributeTypeById(UUID attributeTypeId) {
        return attributeTypeRepository.findById(attributeTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(AttributeType.class, attributeTypeId));
    }

    @Transactional(readOnly = true)
    public AttributeTypeResponse getAttributeTypeResponseById(UUID attributeTypeId) {
        return attributeTypeMapper.toAttributeTypeResponse(getAttributeTypeById(attributeTypeId));
    }

    @Transactional(readOnly = true)
    public Page<AttributeTypeResponse> getAttributeTypeResponsePageable(Pageable pageable) {
        return attributeTypeRepository.findAll(pageable).map(attributeTypeMapper::toAttributeTypeResponse);
    }

    @Transactional(readOnly = true)
    public List<AttributeTypeDetailResponse> getAttributeTypeDetailResponseByCategoryId(UUID categoryId) {
        return attributeTypeRepository.findByListOfCategory_Id(categoryId)
                .stream()
                .map(attributeTypeMapper::toAttributeTypeDetailResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttributeTypeResponse createAttributeType(AttributeTypeRequest attributeTypeRequest) {
        validateAttributeTypeRequest(attributeTypeRequest);
        AttributeType transientAttributeType = attributeTypeMapper.toAttributeType(attributeTypeRequest);
        return attributeTypeMapper.toAttributeTypeResponse(attributeTypeRepository.save(transientAttributeType));
    }

    @Transactional
    public AttributeTypeResponse updateAttributeType(UUID attributeTypeId, AttributeTypeRequest attributeTypeRequest) {
        AttributeType currentAttributeType = getAttributeTypeById(attributeTypeId);
        validateAttributeTypeRequest(attributeTypeRequest);
        return attributeTypeMapper.toAttributeTypeResponse(
                attributeTypeMapper.toExistingAttributeType(attributeTypeRequest, currentAttributeType));
    }

    @Transactional
    public void deleteById(UUID attributeTypeId) {
        if (attributeTypeRepository.existsById(attributeTypeId)) {
            attributeTypeRepository.deleteById(attributeTypeId);
        } else {
            throw new ResourceNotFoundException(AttributeType.class, attributeTypeId);
        }
    }

    private void validateAttributeTypeRequest(AttributeTypeRequest attributeTypeRequest) {
        if (attributeTypeRepository.existsByName(attributeTypeRequest.getName())) {
            throw new ValidationException("Attribute type name already exists!");
        }
    }
}
