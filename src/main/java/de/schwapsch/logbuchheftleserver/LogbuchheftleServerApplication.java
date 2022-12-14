package de.schwapsch.logbuchheftleserver;

import de.schwapsch.logbuchheftleserver.auth.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LogbuchheftleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogbuchheftleServerApplication.class, args);
    }

}
