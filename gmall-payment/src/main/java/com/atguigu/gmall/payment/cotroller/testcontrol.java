package com.atguigu.gmall.payment.cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testcontrol {
    @RequestMapping("/jjj")
    public String index(){
        return "index";
    }
}
