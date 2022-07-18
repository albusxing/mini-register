package com.albusxing.register.client;


/**
 * @author Albusxing
 * @created 2022/6/25
 */
public class RegisterClientTest {

    public static void main(String[] args) throws InterruptedException {

        RegisterClient registerClient = new RegisterClient();
        registerClient.bootstrap();

        Thread.sleep(40 * 1000);
        registerClient.shutdown();
    }
}