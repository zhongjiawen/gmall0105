package com.atguigu.gmall.order.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @DubboReference(version = "113",interfaceClass = CartService.class)
    CartService cartService;

    @DubboReference(version = "999",interfaceClass = UserService.class)
    UserService userService;

    @DubboReference(version = "114",interfaceClass = OrderService.class)
    OrderService orderService;

    @DubboReference(version = "111",interfaceClass = SkuService.class)
    SkuService skuService;

    @RequestMapping("submitOrder")
    @LoginRequired(loginSuccess = true)

    public ModelAndView submitOrder(String receiveAddressId, BigDecimal totalAmount, String tradeCode, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        //检查交易码
        String success = orderService.checkTradeCode(memberId, tradeCode);

        if (success.equals("success")) {

            List<OmsOrderItem> omsOrderItems = new ArrayList<>();

            //订单对象
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setAutoConfirmDay(7);/*7天后自动收货*/
            omsOrder.setDiscountAmount(null);
            omsOrder.setCreateTime(new Date());
            omsOrder.setMemberId(memberId);
            omsOrder.setMemberUsername(nickname);
            omsOrder.setNote("能快点儿送达吗？我急用！！！");
            String outTradeNo = "gmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();/*将毫秒时间戳拼接到外部订单号*/
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + simpleDateFormat.format(new Date());/*将时间字符串拼接到外部订单号*/
            omsOrder.setOrderSn(outTradeNo);
            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(1);
            UmsMemberReceiveAddress umsMemberReceiveAddress = userService.getReceiveAddressById(receiveAddressId);
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            /*当前日期加一天 ，一天后配送*/
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            Date time = calendar.getTime();
            omsOrder.setReceiveTime(time);
            omsOrder.setSourceType(0);/*0:PC 1:APP*/
            omsOrder.setStatus("0");
            omsOrder.setTotalAmount(totalAmount);

            //根据用户id，获取要购买的商品列表（购物车），和总价格
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1")) {
                    //获得订单列表详情
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    //验价(不替用户做决定
                    boolean b = skuService.checkPrice(Long.valueOf(omsCartItem.getProductSkuId()),omsCartItem.getPrice());
                    if (b = false) {
                        ModelAndView modelAndView = new ModelAndView("fail");
                        return modelAndView;
                    }
                    // 验库存,需要远程调用库存系统
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    omsOrderItem.setOrderSn(outTradeNo);//外部订单号，用于和其他系统进行交互，防止重复
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setProductSkuCode("1111111111");
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");//在仓库中的skuId

                    omsOrderItems.add(omsOrderItem);
                }
            }
            omsOrder.setOmsOrderItems(omsOrderItems);


            //将订单和订单详情写入数据库
            //删除购物车的对应商品
            orderService.saveOrder(omsOrder);

            /*重定向到支付系统*/
            ModelAndView modelAndView = new ModelAndView("redirect:http://payment.gmall.com:8087/index");
            modelAndView.addObject("outTradeNo", outTradeNo);
            modelAndView.addObject("totalAmount", totalAmount);
            return modelAndView;


        } else {
            ModelAndView modelAndView = new ModelAndView("fail");
            return modelAndView;
        }

    }

    @RequestMapping("toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        //收件人地址列表
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressByMemberId(memberId);

        //将购物车集合转化为页面计算清单集合
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        /*每循环一个已经被勾选的购物车对象，就封装一个商品详情到omsOrderItems*/
        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);

            }
        }
        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("receiveAddress", umsMemberReceiveAddresses);
        modelMap.put("totalAmount", getTotalAmount(omsCartItems));

        //生成交易码，防止反复提交同一订单。提交订单时做交易码校验
        String tradeCode = orderService.genTradeCode(memberId);
        modelMap.put("tradeCode", tradeCode);
        return "trade";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {

        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if (omsCartItem.getIsChecked().equals("1"))
                totalAmount = totalAmount.add(totalPrice);
        }

        return totalAmount;
    }

}
