package be.jsilkens.devbooks.shopping.db;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
@org.springframework.test.context.TestPropertySource(properties = {
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration"
})
public abstract class IntegrationTestBase {

}
