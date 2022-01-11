package io.spring.jbuy.common.mapper;

import io.spring.jbuy.common.entity.ID;
import io.spring.jbuy.features.user.UserProfile;
import org.mapstruct.TargetType;

import java.util.UUID;

public interface ReferenceMapper {

    <T extends ID> T idToEntity(UUID id, @TargetType Class<T> entityClass);

    <T extends UserProfile> T mapsIdToEntity(UUID id, @TargetType Class<T> entityClass);

    <T> T findById(UUID id, Class<T> entityClass);
}
