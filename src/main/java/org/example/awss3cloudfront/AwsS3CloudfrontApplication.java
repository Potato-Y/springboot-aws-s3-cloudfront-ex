package org.example.awss3cloudfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AwsS3CloudfrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsS3CloudfrontApplication.class, args);
    }

}
