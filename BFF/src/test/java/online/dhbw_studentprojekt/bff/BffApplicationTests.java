package online.dhbw_studentprojekt.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("local")
class BffApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMainMethod() {
        String[] args = {}; // Provide arguments if necessary
        BFFApplication.main(args);
    }
}
