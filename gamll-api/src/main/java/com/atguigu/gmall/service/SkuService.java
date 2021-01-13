package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {


    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);


    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(Long productId);

    List<PmsSkuInfo> getAllSku(String catalog3Id);

    boolean checkPrice(Long productSkuId, BigDecimal price);

    PmsSkuInfo getSkuById(String skuId, String ip);
}
