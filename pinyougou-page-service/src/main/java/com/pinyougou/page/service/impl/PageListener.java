package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听类
 * 用于生成网页
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService pageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            String text = textMessage.getText();
            System.out.println("接受到消息"+text);
            boolean b = pageService.genItemHtml(Long.parseLong(text));
            System.out.println("网页生成结果"+ b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
