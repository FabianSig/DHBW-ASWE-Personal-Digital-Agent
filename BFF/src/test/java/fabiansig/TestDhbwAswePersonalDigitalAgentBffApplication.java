package fabiansig;

import org.springframework.boot.SpringApplication;

public class TestDhbwAswePersonalDigitalAgentBffApplication {

    public static void main(String[] args) {
        SpringApplication.from(PersonalDigitalAgentBFFApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
