package org.tanfuhua.lowcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 激活 @FeignClient
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LowcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LowcodeApplication.class, args);
    }

}
