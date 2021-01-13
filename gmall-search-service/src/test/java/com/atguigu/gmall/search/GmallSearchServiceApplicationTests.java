package com.atguigu.gmall.search;

import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class GmallSearchServiceApplicationTests {

    @DubboReference(version = "111",interfaceClass = SkuService.class)
    SkuService skuService;

    @Autowired
    JestClient jestClient;


    @Test
    void contextLoads() {
        try {
            jestClient.execute(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
