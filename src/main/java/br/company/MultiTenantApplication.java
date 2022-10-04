package br.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MultiTenantApplication {

    private static ConfigurableApplicationContext run;

    public static void main(String[] args) {
        run = SpringApplication.run(MultiTenantApplication.class, args);
    }

    public static ConfigurableApplicationContext getRun() {
        return run;
    }

}
