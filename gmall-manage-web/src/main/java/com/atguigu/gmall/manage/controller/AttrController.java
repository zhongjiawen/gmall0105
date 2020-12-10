package com.atguigu.gmall.manage.controller;


import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {
    @DubboReference(interfaceClass = AttrService.class,version = "${demo.service.version}" )
    AttrService attrService;
//    /saveAttrInfo

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo>  attrInfoList(String catalog3Id)
    {
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        //对象名没意思 只是都是data 只要保证内部对象正常就行
        return pmsBaseAttrInfos;
    }
    //保存属性的
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo)
    {
        //dao层需要用的是insert用返回值
     String success   =  attrService.saveAttrInfo(pmsBaseAttrInfo);


       return  "success";
    }

    //getAttrValueList
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue>  getAttrValueList(String attrIdId)
    {
        List<PmsBaseAttrValue> PmsBaseAttrValues = attrService.getAttrValueList(attrIdId);
        return PmsBaseAttrValues;
    }
    //baseSaleAttrList 用户选择平台属性
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr>  baseSaleAttrList(String catalog3Id)
    {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList(catalog3Id);
        return pmsBaseSaleAttrs;
    }




}
