package mongo.proyect.servicioDocumental;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class ServicioDocumentalApplication extends WebMvcConfigurerAdapter{
        @Value("${staticresourceloader.imageFileLocation.path}")
        private String staticImageFilePath;

	public static void main(String[] args) {
		SpringApplication.run(ServicioDocumentalApplication.class, args);
	}
         
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/**").addResourceLocations(staticImageFilePath);
        }  

}
