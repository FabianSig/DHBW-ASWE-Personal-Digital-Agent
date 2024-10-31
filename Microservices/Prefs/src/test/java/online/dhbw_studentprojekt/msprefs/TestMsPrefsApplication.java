package online.dhbw_studentprojekt.msprefs;

import org.springframework.boot.SpringApplication;

public class TestMsPrefsApplication {

    public static void main(String[] args) {
        SpringApplication.from(MsPrefsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
