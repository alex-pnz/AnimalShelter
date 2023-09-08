package pro.sky.animalshelter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info=@Info(
                title = "Animal Shelter API",
                description = "Animal Shelter",
                version = "1.0.0",
                contact = @Contact(
                        name = "Dev Pro",
                        url = "https://github.com/alex-niculita/AnimalShelter"
                )
        )
)
public class OpenApiConfig {
}
