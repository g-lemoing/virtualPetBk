package cat.itacademy.S05T02.virtualPetBk.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("virtualPet")
                .pathsToMatch("/auth/**", "/player/**", "/ranking")
                .build();
    }
}