package app.rental.projet_3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.rental.projet_3.services.ImageStorageService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final String uploadDir;

	public WebConfig(@Value("${app.upload-dir}") String uploadDir) {
		this.uploadDir = uploadDir;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(ImageStorageService.URL_PATH + "**")
				.addResourceLocations("file:" + uploadDir + "/");
	}
}
