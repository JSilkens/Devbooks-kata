package be.jsilkens.devbooks.shopping.db;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import(FlywayAutoConfiguration.class)
@ComponentScan("be.jsilkens.devbooks.shopping.db")
public class TestConfig {
}
