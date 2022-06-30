package com.albusxing.register.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liguoqing
 */
@SpringBootApplication
public class RegisterServerApplication {

    public static void main(String[] args) {

        ServiceAliveMonitor serviceAliveMonitor = new ServiceAliveMonitor();
        serviceAliveMonitor.start();
        SpringApplication.run(RegisterServerApplication.class, args);
    }
}
