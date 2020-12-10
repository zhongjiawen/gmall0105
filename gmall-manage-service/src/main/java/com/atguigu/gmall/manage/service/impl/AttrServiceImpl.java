package com.atguigu.gmall.manage.service.impl;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
//dubbd的配置 版本 以及接口
@DubboService(version = "${demo.service.version}",interfaceClass = AttrService.class)
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;


    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override //三级目录的id
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        //
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        //三级id 查询到 平台属性 列表 外壳 颜色 尺寸等手机信息
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        for(PmsBaseAttrInfo baseAttrInfo :pmsBaseAttrInfos){
            //新建列表 通过平台属性的 id 如电脑的 id查找 到对应的属性的的值
            List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);

            //封装起来
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id= pmsBaseAttrInfo.getId();
        if(StringUtils.isBlank(id))
        { // id 为空
            //保存属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);//insert 都插入 insertselective null 不插入数据库

            //保存属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                //开启主键返回策略
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());

                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }else{
            //id不空 修改

            //修改属性
            Example example  = new Example(PmsBaseAttrInfo.class);
            //正则表达式
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
                                                             //根据条件id 修改为pmsBaseAttrInfo的样子
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

               //修改属性值

              List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
              //按照属性id 删除属性值
              PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
              pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
              pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel );

              //删除后将新的属性值插入
              for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {


              pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                 // pmsBaseAttrValueMapper.update()

            }



        }



        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrIdId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrIdId);
        List<PmsBaseAttrValue>  pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList(String catalog3Id) {
//         PmsBaseSaleAttr pmsBaseSaleAttr = new PmsBaseSaleAttr();
//         pmsBaseSaleAttr.setId(catalog3Id);
//         List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.select(pmsBaseSaleAttr);
        //通用的属性的选择框 所以选择所有
         return  pmsBaseSaleAttrMapper.selectAll();
    }
}
