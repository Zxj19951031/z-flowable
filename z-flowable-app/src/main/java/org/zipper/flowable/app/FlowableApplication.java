package org.zipper.flowable.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
public class FlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }
}
