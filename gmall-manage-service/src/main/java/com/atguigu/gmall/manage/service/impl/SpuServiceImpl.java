package com.atguigu.gmall.manage.service.impl;

import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.CatalogService;
import com.atguigu.gmall.service.SpuService;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(version = "${demo.service.version}",interfaceClass = SpuService.class)
public class SpuServiceImpl implements SpuService {
    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new  PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfos;
    }

     public void saveSpuInfo(PmsProductInfo pmsProductInfo){
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        //保存上传图片的信息：spuImageList
        for(PmsProductImage pmsProductImage:pmsProductInfo.getSpuImageList()){
            PmsProductImage p =new PmsProductImage();
            p.setImgName(pmsProductImage.getImgName());
            p.setImgUrl(pmsProductImage.getImgUrl());
            p.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insertSelective(p);
        }

        //添加商品销售属性值:多个销售属性，每个属性有不同值
        for(PmsProductSaleAttr pmsProductSaleAttr:spuSaleAttrList){
            PmsProductSaleAttr temp = new PmsProductSaleAttr();
            temp.setProductId(Long.parseLong(pmsProductInfo.getId()));
            temp.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
            temp.setSaleAttrName(pmsProductSaleAttr.getSaleAttrName());
            pmsProductSaleAttrMapper.insertSelective(temp);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for(PmsProductSaleAttrValue pmsProductSaleAttrValue:pmsProductSaleAttrValueList){
                PmsProductSaleAttrValue p = new PmsProductSaleAttrValue();
                p.setProductId(pmsProductInfo.getId());
                p.setSaleAttrId(pmsProductSaleAttrValue.getSaleAttrId());
                p.setSaleAttrValueName(pmsProductSaleAttrValue.getSaleAttrValueName());
                pmsProductSaleAttrValueMapper.insertSelective(p);
            }

        }
  }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        //这里因为导入的为  long  需要把string转为long
        pmsProductSaleAttr.setProductId(Long.parseLong(spuId));
         List<PmsProductSaleAttr> pmsProductSaleAttrs =   pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
         for(PmsProductSaleAttr productSaleAttr:pmsProductSaleAttrs){
             PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();

             pmsProductSaleAttrValue.setId(spuId);
             //强制转
             pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId().toString());//这里的是销售属性 用的是系统表中的主键 不是销售表属性
             List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
             productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);

         }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> getSpuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(String.valueOf(Long.parseLong(spuId)));
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(Long.valueOf(productId));
//        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
//
//     for(PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs){
//         Long saleAttrId = productSaleAttr.getSaleAttrId();
//         PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//         pmsProductSaleAttrValue.setSaleAttrId(String.valueOf(saleAttrId));
//         pmsProductSaleAttrValue.setProductId(productId);
//
//        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues =  pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//        productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
//
//     }


   // List<PmsBaseSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
     List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);

        return pmsProductSaleAttrs;
    }
}
