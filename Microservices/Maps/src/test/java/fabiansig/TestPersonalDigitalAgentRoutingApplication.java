package fabiansig;

import org.springframework.boot.SpringApplication;

public class TestPersonalDigitalAgentRoutingApplication {

	public static void main(String[] args) {
		SpringApplication.from(PersonalDigitalAgentRoutingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
