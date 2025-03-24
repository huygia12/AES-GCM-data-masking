package encryption.algorithm.demos.aesgcmmasking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		info = @Info(contact = @Contact(name = "Huy", email = "huychamngoan12@gmail.com"),
				description = "Open api documentation for aes-gcm demo", title = "Aes-gcm data masking backend apis",
				license = @License(name = "Api license"), termsOfService = "Terms of service"),
		tags = { @Tag(name = "User Management", description = "signup, edit and get users information") })
@Configuration
public class OpenAPIConfiguration {

	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("encryption.algorithm.demos")
			.packagesToScan("encryption.algorithm.demos.aesgcmmasking.controller", "org.springframework.boot")
			.build();
	}

}
