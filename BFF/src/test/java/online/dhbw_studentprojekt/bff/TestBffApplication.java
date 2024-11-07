package online.dhbw_studentprojekt.bff;

import org.springframework.boot.SpringApplication;

public class TestBffApplication {

    public static void main(String[] args) {
        SpringApplication.from(BFFApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
