package com.atguigu.gmall.gmall.item.ItemController;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class ItemController {
//
//    @RequestMapping("index")
//    public  String index(ModelMap modelMap){
//
//        List<String> list = new ArrayList<String>();
//        for(int i = 0;i<5;i++){
//           list.add("数据"+i);
//        }
//
//        modelMap.put("list",list);
//        modelMap.put("hello","sb 的一天");
//        modelMap.put("checkbox","1");
//        return "index";
//    }

    @DubboReference(interfaceClass =SkuService.class,version = "${demo.service.version}" )
    SkuService skuService;

    @DubboReference(interfaceClass = SpuService.class,version = "${demo.service.version}" )
    SpuService spuService;
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map , HttpServletRequest request) {

        String remoteAddr = request.getRemoteAddr();
      //  request.getHeader("");//nginx负载均衡

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId,remoteAddr);
        //sku对象
        map.put("skuInfo",pmsSkuInfo);
        //销售属性列表                //pmsProductSaleAttrs
        List<PmsProductSaleAttr>  pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        //查询当前spu的其他的hash表集合
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(Long.valueOf(pmsSkuInfo.getProductId()));
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String hashKey = "";
            String hashValue = skuInfo.getId();
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValues) {
                hashKey += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";//5|7|9...
            }
            skuSaleAttrHash.put(hashKey, hashValue);
        }
        //将sku的销售属性放到页面上
          String skuSaleAttrHashJsonStr  =   JSON.toJSONString(skuSaleAttrHash);
          map.put("jsonStr",skuSaleAttrHashJsonStr);


        return "item";
    }

}
