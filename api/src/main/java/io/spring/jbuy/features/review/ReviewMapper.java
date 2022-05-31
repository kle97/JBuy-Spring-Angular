package io.spring.jbuy.features.review;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "review.product.name")
    @Mapping(target = "images", source = "review.product.images")
    @Mapping(target = "price", source = "review.product.discountPrice")
    @Mapping(target = "firstName", source = "review.user.firstName")
    @Mapping(target = "lastName", source = "review.user.lastName")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    Review toReview(ReviewRequest reviewRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    Review toExistingReview(ReviewRequest reviewRequest, @MappingTarget Review review);
}
