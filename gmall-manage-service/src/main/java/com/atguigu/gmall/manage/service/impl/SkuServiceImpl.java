package com.atguigu.gmall.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@DubboService(version = "${demo.service.version}",interfaceClass = SkuService.class)
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;




    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //存储sku_info  转string
        pmsSkuInfo.setProductId(String.valueOf(Long.parseLong(pmsSkuInfo.getSpuId())));
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        //存储图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for(PmsSkuImage pmsSkuImage:skuImageList){
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

        for(PmsSkuAttrValue pmsSkuAttrValue:skuAttrValueList){
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:skuSaleAttrValueList){
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

    }

//    @Override
    public PmsSkuInfo getSkuByIdDb(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

//        sku图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> select = pmsSkuImageMapper.select(pmsSkuImage);

        skuInfo.setSkuImageList(select);
        return skuInfo;


    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(Long productId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(Long productSkuId, BigDecimal price) {
        boolean b = false;//计算机通常消极处理
//
//        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
//        pmsSkuInfo.setId(productSkuId);
//
//        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
//        BigDecimal price = pmsSkuInfo1.getPrice();
//        if (price.compareTo(productPrice) == 0) {
//            b = true;
//        }
        return true;
    }




//    @Override                   //, String ip
//    public PmsSkuInfo getSkuById(String skuId,String ip) {
//
//                System.out.println("ip为"+ip+"ip的同学:"+Thread.currentThread().getName()+"进入商品的请求");
//                PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
//                 /*1.连接缓存*/
//
//                Jedis jedis  = redisUtil.getJedis();
//                //查询缓存  下面自己构造的函数
//                String skuKey = "sku:" + skuId + ":info";
//                String skuJson = jedis.get(skuKey);
//                if(StringUtils.isNotBlank(skuJson)) {
//                    pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
//                    System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "从redis缓存中获取商品详情");
//                }else{
//                    //去myslq中找
//                    System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "申请缓存分布式锁");
//                    //设置分布式锁
//                     String  OK  =  jedis.set("sku:" + skuId + ":lock","1","nx","px",10*1000);//锁的线程秒
//                    if (OK != null && !OK.equals("") && OK.equals("OK")) {
//                        //设置成功后 可以在10秒访问数据库
//                         System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "申请缓存分布式锁");
//                         pmsSkuInfo =  getSkuByIdDb(skuId);
//                    if(pmsSkuInfo!=null){
//                        System.out.println("ip为:" +"的用户:" + Thread.currentThread().getName() + "有十秒的过期访问数据库");
//                        jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
//
//                       }else{
//                        //数据库中不存在该sku
//                        //为了防止穿透将null 设置给redis
//                        //三分钟内
//                         jedis.setex("sku:" + skuId + ":info", 60*3,JSON.toJSONString(""));
//                        //在访问后   将mysql 的分布式锁释放
//                         jedis.del("sku:"+skuId+":lock");
//                        try {
//                            Thread.sleep(1000*5);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "使用完毕将锁归还");
//                         }
//                     }else{
//                         System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "没有拿到锁开始自旋");
//                         //设置失败 在几秒后重新尝试访问
//                         try {
//                             Thread.sleep(3000);
//                         } catch (InterruptedException e) {
//                             e.printStackTrace();
//                         }
//                         //自旋 防止创建新线程
//                         return  getSkuById(skuId,ip);
//                     }
//
//                }
//
//                //如果缓存没有查询mysql
//                //mysql 查询结果
//
//        jedis.close();
//        return pmsSkuInfo;
//    }

    @Override
    public PmsSkuInfo getSkuById(String skuId, String ip) {
        System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "进入了商品详情的请求");
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        /*1.连接缓存*/
        Jedis jedis = redisUtil.getJedis();
        /*2.查询缓存*/
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = jedis.get(skuKey);
        /*StringUtils.isEmpty(skuJson)*/
        if (skuJson != null && !skuJson.equals("")) {
            /*把json转化为java对象类  , 但是skuJson不能为空*/
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "从redis缓存中获取商品详情");
        } else {
            /*3.如果缓存中没有，查询mysql*/
            System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "发现缓存中没有申请缓存分布式锁：" + "sku:" + skuId + ":lock");
            /**
             * 缓存穿透问题：（1）.基于redis的nx分布式锁
             */
            /*设置分布式锁 返回值是OK*/
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10 * 1000);
            if (OK != null && !OK.equals("") && OK.equals("OK")) {
                /*设置成功，有权利在10秒的过期时间内访问数据库*/
                System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "成功拿到分布式锁，有权利在10秒的过期时间内访问数据库" + "sku:" + skuId + ":lock");
                pmsSkuInfo = getSkuByIdDb(skuId);

//                /*测试代码：*/
//               try {
//                    Thread.sleep(5*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                if (pmsSkuInfo != null) {
                    /*4.mysql将想要查询的结果返回给redis*/
                    System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "mysql将数据结果返回给redis缓存中");
                    jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
                } else {
                    /*数据库中不存在该sku*/
                    /*为了防止缓存穿透(大量请求访问数据库mysql)，将null值或者空串给redis , 并且给null值或者空串设置3分钟的自动销毁*/
                    jedis.setex("sku:" + skuId + ":info", 60 * 3, JSON.toJSONString(""));
                }
                /*在访问MySQL后，将手中的mysql锁释放掉*/
                /*问题一：为了避免当拿到锁的用户在过期时间后依旧没有从数据库中获取到数据，锁已经自动销毁（过期时间已满）
                  这时下一个用户拿到锁，开始访问数据库，但是，上一个拿到锁的用户访问完毕，会回来执行“删除锁”
                  jedis.del("sku:" + skuId + ":lock");命令，这时删除的锁是当前正在访问的用户的锁，造成当前用户访问失败。
                  所以在当前用户拿到锁情况下，上一个用户回来做“删除锁”jedis.del("sku:" + skuId + ":lock");命令时
                  上一个用户应该将此时的newToken和自己拿到锁分配的随机token做比较，相同则是同一个用户，可以删除锁
                  确认删除的是自己的锁    "sku:" + skuId + ":info"=token  k , v 结构 */
                String newToken = jedis.get("sku:" + skuId + ":lock");
                if (newToken != null && !newToken.equals("") && newToken.equals(token)) {
                    /*问题二：当刚好代码执行到这里时，自己的token过期了，然后在if里面又把下一个用户的token删除了，怎么解决？
                      可以用lua脚本，在查询到key的同时删除该key，防止高并发下的意外发生！
                      将：String newToken = jedis.get("sku:" + skuId + ":info");
                         if(newToken!=null&&!newToken.equals("")&&newToken.equals(token)){
                             jedis.del("sku:" + skuId + ":lock");
                         }改为下面的代码：lua脚本，在查询到key的同时删除该key！
                       String script = "if redis.call('get' , KEYS[1]==ARGV[1] then return redis.call('del',KEYS[1]))
                                        else return o end";
                       jedis.eval(script,Collections.singletonList("lock"),Conllections.singletonList(token));
                    */
                    jedis.del("sku:" + skuId + ":lock");
                    System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "在访问MySQL后，将手中的mysql锁释放掉" + "sku:" + skuId + ":lock");
                }
            } else {
                /*设置失败 ，自旋（该线程在睡眠几秒后重新尝试访问getSkuById方法）*/
                System.out.println("ip为:" + ip + "的用户:" + Thread.currentThread().getName() + "没有拿到分布式锁 ，自旋（该线程在睡眠几秒后重新尝试访问getSkuById方法）");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*return不会产生新的线程，这才是自旋。如果不加return，则会产生新的getSkuById（）“孤儿”线程。*/
                return getSkuById(skuId, ip);
            }
        }

        jedis.close();/*最后一步一定要关闭jidis*/

        return pmsSkuInfo;
    }
}
