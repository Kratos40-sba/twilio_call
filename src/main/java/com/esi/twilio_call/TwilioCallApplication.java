package com.esi.twilio_call;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwilioCallApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        
        SpringApplication.run(TwilioCallApplication.class, args);
    }

}
/*
export GOOGLE_APPLICATION_CREDENTIALS=/home/abdou/Downloads/spring-boot-k8s-demo1-83a66a3ad705.json
 */