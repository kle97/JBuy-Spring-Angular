package io.spring.jbuy.features.product_attribute;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.attribute.Attribute;
import io.spring.jbuy.features.product.Product;
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
@RequiredArgsConstructor @Slf4j
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;

    @Transactional(readOnly = true)
    public ProductAttribute getProductAttributeById(UUID productId, UUID attributeId) {
        return productAttributeRepository.findById_ProductIdAndId_AttributeId(productId, attributeId)
                .orElseThrow(() -> new ResourceNotFoundException(ProductAttribute.class, Product.class,
                                                                 productId, Attribute.class, attributeId));
    }

    @Transactional(readOnly = true)
    public ProductAttributeResponse getProductAttributeResponseById(UUID productId, UUID attributeId) {
        return productAttributeMapper.toProductAttributeResponse(getProductAttributeById(productId, attributeId));
    }

    @Transactional(readOnly = true)
    public Page<ProductAttributeResponse> getProductAttributeResponsePageable(Pageable pageable) {
        return productAttributeRepository.findAll(pageable).map(productAttributeMapper::toProductAttributeResponse);
    }

    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getAllProductAttributesByProductId(UUID productId) {
        return productAttributeRepository.findAllWithRelationshipByProduct_IdOrderByCreatedAt(productId)
                .stream()
                .map(productAttributeMapper::toProductAttributeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductAttributeResponse createProductAttribute(ProductAttributeRequest productAttributeRequest) {
        validateProductAttributeRequest(productAttributeRequest);
        ProductAttribute transientProductAttribute = productAttributeMapper.toProductAttribute(productAttributeRequest);
        return productAttributeMapper.toProductAttributeResponse(
                productAttributeRepository.save(transientProductAttribute));
    }

    @Transactional
    public ProductAttributeResponse updateProductAttribute(UUID productId, UUID attributeId,
                                                           ProductAttributeRequest productAttributeRequest) {
        ProductAttribute currentProductAttribute = getProductAttributeById(productId, attributeId);
        return productAttributeMapper.toProductAttributeResponse(
                productAttributeMapper.toExistingProductAttribute(productAttributeRequest, currentProductAttribute));
    }

    @Transactional
    public void deleteByProductIdAndAttributeId(UUID productId, UUID attributeId) {
        if (productAttributeRepository.existsById_ProductIdAndId_AttributeId(productId, attributeId)) {
            productAttributeRepository.deleteById_ProductIdAndId_AttributeId(productId, attributeId);
        } else {
            throw new ResourceNotFoundException(ProductAttribute.class, Product.class,
                                                productId, Attribute.class, attributeId);
        }
    }

    private void validateProductAttributeRequest(ProductAttributeRequest productAttributeRequest) {
        if (productAttributeRepository.existsById_ProductIdAndId_AttributeId(
                productAttributeRequest.getProductId(),
                productAttributeRequest.getAttributeId())) {
            throw new ValidationException("Product attribute already exists!");
        }
    }
}
