package com.atguigu.gmall.manage.controller;


import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.manage.util.PmsUploadUtil;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SpuService;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@CrossOrigin
public class SpuController {

    @DubboReference(interfaceClass = SpuService.class,version = "${demo.service.version}" )
    SpuService spuService;



    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id)
    {
        List<PmsProductInfo>  pmsProductInfos = spuService.spuList(catalog3Id);

        return pmsProductInfos;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo)
    {
        spuService.saveSpuInfo(pmsProductInfo);

        return "success";
    }
    @RequestMapping(value = "fileUpload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file)  {
        //将图片或者视频上传到系统中
        String imgUrl = PmsUploadUtil.uploadImage(file);
        System.out.println(imgUrl);
        //将图片中的路径返回给页面

        return imgUrl;
    }
    //spuSaleAttrList
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId)  {


        List<PmsProductSaleAttr> PmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);

        return PmsProductSaleAttrs ;
    }
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> list =  spuService.getSpuImageList(spuId);
        return list;
    }
}
