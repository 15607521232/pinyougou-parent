package com.pinyougou.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;


    @RequestMapping("/createNative")
    public Map createNative(){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        if(payLog!=null){
            return weixinPayService.createNative(payLog.getOutTradeNo(),payLog.getTotalFee()+"");
        }

        return new HashMap();
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int x = 0;
        while (true){
            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
            if(map==null){
                result = new Result(false,"支付出错");
                break;
            }

            if(map.get("trade_state").equals("SUCCESS")){
                result = new Result(true,"支付成功");
                //修改订单状态
                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));
                break;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //为了不让循环无休止的执行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为五分钟
            x++;
            if(x>=100){
                result = new Result(false,"二维码超时");
                break;
            }
        }
        return result;
    }
}
