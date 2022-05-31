package io.spring.jbuy.features.category;

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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryResponseById(UUID categoryId) {
        return categoryMapper.toCategoryResponse(getCategoryById(categoryId));
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategoryResponsePageable(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toCategoryResponse);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        validateCategoryRequest(categoryRequest);
        Category transientCategory = categoryMapper.toCategory(categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(transientCategory));
    }

    @Transactional
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest categoryRequest) {
        Category currentCategory = getCategoryById(categoryId);
        validateCategoryRequest(categoryRequest);
        return categoryMapper.toCategoryResponse(categoryMapper.toExistingCategory(categoryRequest, currentCategory));
    }

    @Transactional
    public void deleteById(UUID categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new ResourceNotFoundException(Category.class, categoryId);
        }
    }

    private void validateCategoryRequest(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ValidationException("Category name already exists!");
        }
    }
}
