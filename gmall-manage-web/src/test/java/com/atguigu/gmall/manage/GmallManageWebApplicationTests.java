package com.atguigu.gmall.manage;


import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GmallManageWebApplicationTests {


    @Test
    void contextLoads() throws IOException, MyException {
        //配置fdfs的全局链接地址
       String tracker= GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();//获取配置文件路径
        ClientGlobal.init(tracker);
       TrackerClient trackerClient=  new TrackerClient();

       //获取一个trackerSer的实例
        String url = "http://192.168.147.133";
       TrackerServer trackerServer = trackerClient.getTrackerServer();
       StorageClient storageClient = new StorageClient(trackerServer,null);
        String[]uploadinfo  =  storageClient.upload_file("G:\\gmall0105\\gmall-manage-web\\src\\main\\resources\\01.png","png",null);
        for(String up : uploadinfo){
            url+="/"+up;
        }
        System.out.println(url);
    }

}
