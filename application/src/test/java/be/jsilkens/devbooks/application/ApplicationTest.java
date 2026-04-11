package be.jsilkens.devbooks.application;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
class ApplicationTest extends IntegrationTestBase {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
