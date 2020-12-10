package com.atguigu.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile) {
        String imgUrl = "";
        //上传服务器
        //配置fdfs的全局链接地址
        String tracker= PmsUploadUtil.class.getResource("/tracker.conf").getPath();//获取配置文件路径
        try {
            ClientGlobal.init(tracker);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient=  new TrackerClient();

        //获取一个trackerSer的实例
        String url = "http://192.168.147.133";
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageClient storageClient = new StorageClient(trackerServer,null);

        try {
            byte[] bytes = multipartFile.getBytes();//获得上传的二进制对象
            String originalFIlename = multipartFile.getOriginalFilename();//a.jpg
                                                                          //获取最后一个点的位置加一
            System.out.println(originalFIlename);
            int i = originalFIlename.lastIndexOf(".");
            String exName = originalFIlename.substring(i+1);


            String[] uploadinfo  = storageClient.upload_file(bytes,"png",null);
            for(String up : uploadinfo){
                url+="/"+up;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

         imgUrl=url;

        return imgUrl;
    }
}
