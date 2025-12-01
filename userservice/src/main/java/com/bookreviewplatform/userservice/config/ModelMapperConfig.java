package com.bookreviewplatform.userservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for setting up and providing shared beans
 * related to object mapping in the User Service module.
 *
 * <p>This class defines a {@link ModelMapper} bean that can be autowired
 * autowired into any Spring-managed component (services, controllers, etc.)
 * to perform mapping between DTOs, entities, and other object types.</p>
 *
 * <p>ModelMapper is a popular library that simplifies object-to-object mapping
 * by automatically mapping fields with matching names and offering advanced
 * configuration when custom mapping rules are required.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates and configures a singleton {@link ModelMapper} instance.
     *
     * <p>The returned bean is managed by the Spring container and shared
     * across the entire application context, ensuring consistent mapping behavior
     * and avoiding the overhead of creating multiple instances.</p>
     *
     * <p>Additional customizations (e.g., strict matching, custom converters,
     * type maps, etc.) can be added here in the need arises.</p>
     *
     * @return a fully configured {@link ModelMapper} instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Example of possible future customizations:
        // modelMapper.getConfiguration()
        //            .setMatchingStrategy(MatchingStrategies.STRICT)
        //            .setFieldMatchingEnabled(true)
        //            .setSkipNullEnabled(true);

        // Custom type maps or converters can be registered here
        // createTypeMap(User.class, UserResponseDTO.class)
        //     .addMappings(mapper -> mapper.map(src -> src.getProfile().getFullName(),
        //                                        UserResponseDTO::setFullName));

        return modelMapper;
    }
}