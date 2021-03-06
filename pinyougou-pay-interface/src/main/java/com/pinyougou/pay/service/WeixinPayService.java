package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 生成微信二维码
     */

    public Map createNative(String out_trade_no,String total_fee);

    public Map queryPayStatus(String out_trade_no);

    public Map closePay(String out_trade_no);
}
