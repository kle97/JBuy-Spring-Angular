package io.spring.jbuy.common.mapper;

import io.spring.jbuy.common.entity.ID;
import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.user.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

@Component
@Transactional(readOnly = true)
@Slf4j
public class ReferenceMapperImpl implements ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T extends ID> T idToEntity(UUID id, @TargetType Class<T> entityClass) {
        return findById(id, entityClass);
    }

    @Override
    public <T extends UserProfile> T mapsIdToEntity(UUID id, @TargetType Class<T> entityClass) {
        return findById(id, entityClass);
    }

    @Override
    public <T> T findById(UUID id, Class<T> entityClass) {
        if (id == null) {
            return null;
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put(CACHEABLE, true);
        T entity = entityManager.find(entityClass, id, properties);

        if (entity != null) {
            return entity;
        } else {
            throw new ResourceNotFoundException(entityClass, id);
        }
    }
}
