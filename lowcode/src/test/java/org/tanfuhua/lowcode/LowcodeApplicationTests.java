package org.tanfuhua.lowcode;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LowcodeApplicationTests {

    @Test
    void contextLoads() {
        try {
            NamingService namingService = NacosFactory.createNamingService("localhost:8848");
            System.out.println();

        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

}
