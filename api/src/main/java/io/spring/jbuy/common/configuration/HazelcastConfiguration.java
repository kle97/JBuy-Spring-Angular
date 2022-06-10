package io.spring.jbuy.common.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.SpringSessionHazelcastInstance;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.util.List;

@Configuration
@EnableHazelcastHttpSession
@RequiredArgsConstructor
@Slf4j
public class HazelcastConfiguration {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final SystemProperties systemProperties;

    @Bean
    public SessionRepositoryCustomizer<HazelcastIndexedSessionRepository> customize() {
        return (sessionRepository) -> {
            // Set session timeout. Default session timeout is 1800s or 30 min
            // seems to not work, use properties 'server.servlet.session.cookie.max-age' or
            // 'server.servlet.session.timeout' in application.properties instead
            sessionRepository.setDefaultMaxInactiveInterval(systemProperties.getSessionTimeout());
        };
    }

    @Bean
    @SpringSessionHazelcastInstance
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setInstanceName(systemProperties.getHazelcastInstanceName());
        if (activeProfile.equals("dev")) {
            config.getNetworkConfig().setPort(systemProperties.getHazelcastPort());
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig()
                    .getJoin()
                    .getTcpIpConfig()
                    .setEnabled(true)
                    .setMembers(
                            List.of(systemProperties.getHazelcastHost() + ":" + systemProperties.getHazelcastPort()));
            if (systemProperties.isHazelcastManEnabled()) {
                config.getManagementCenterConfig().setEnabled(true).setUrl(systemProperties.getHazelcastManUrl());
            }
        }

        initializeDomainMapConfig(config);
        initializeSpringSessionHazelcastConfig(config);
        return Hazelcast.newHazelcastInstance(config);
    }

    private void initializeSpringSessionHazelcastConfig(Config config) {
        // Add this attribute to be able to query sessions by their PRINCIPAL_NAME_ATTRIBUTE's
        MapAttributeConfig attributeConfig = new MapAttributeConfig()
                .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractor(PrincipalNameExtractor.class.getName());

        // Configure the sessions map
        config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
                .addMapAttributeConfig(attributeConfig)
                .addMapIndexConfig(new MapIndexConfig(
                        HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

        // Use custom serializer to de/serialize sessions faster. This is optional.
        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
        config.getSerializationConfig().addSerializerConfig(serializerConfig);
    }

    private void initializeDomainMapConfig(Config config) {
        MapConfig smallMapConfig = new MapConfig()
                .setBackupCount(0)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizeConfig(new MaxSizeConfig().setSize(300))
                .setTimeToLiveSeconds(300);

        MapConfig mediumMapConfig = new MapConfig()
                .setBackupCount(0)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizeConfig(new MaxSizeConfig().setSize(1000))
                .setTimeToLiveSeconds(600);

        MapConfig largeMapConfig = new MapConfig()
                .setBackupCount(0)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizeConfig(new MaxSizeConfig().setSize(5000))
                .setTimeToLiveSeconds(900);

        // query cache
        config.addMapConfig(new MapConfig(largeMapConfig).setName("default-query-results-region"));

        // entities and collections cache
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.user.User"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.user.User.listOfAuthority"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.user.UserProfile"));
        config.addMapConfig(new MapConfig(smallMapConfig).setName("io.spring.jbuy.features.authority.Authority"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.authority.Authority.listOfUser"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.address.Address"));
        config.addMapConfig(new MapConfig(largeMapConfig)
                                    .setName("io.spring.jbuy.features.attribute.Attribute.listOfProductAttribute"));
        config.addMapConfig(new MapConfig(smallMapConfig).setName("io.spring.jbuy.features.attribute_type.AttributeType"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.attribute_type.AttributeType.listOfAttribute"));
        config.addMapConfig(new MapConfig(smallMapConfig)
                                    .setName("io.spring.jbuy.features.attribute_type.AttributeType.listOfCategory"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.cart_item.CartItem"));
        config.addMapConfig(new MapConfig(smallMapConfig).setName("io.spring.jbuy.features.category.Category"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.category.Category.listOfProduct"));
        config.addMapConfig(new MapConfig(smallMapConfig)
                                    .setName("io.spring.jbuy.features.category.Category.listOfAttributeType"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.customer.Customer"));
        config.addMapConfig(new MapConfig(smallMapConfig).setName("io.spring.jbuy.features.keyword.Keyword"));
        config.addMapConfig(new MapConfig(mediumMapConfig).setName("io.spring.jbuy.features.order.Order"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.order_product.OrderProduct"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product.listOfProductAttribute"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product.listOfCartItem"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product.listOfOrderProduct"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product.listOfReview"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product.Product.listOfCategory"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.product_attribute.ProductAttribute"));
        config.addMapConfig(new MapConfig(mediumMapConfig)
                                    .setName("io.spring.jbuy.features.review.Review"));
    }
}
