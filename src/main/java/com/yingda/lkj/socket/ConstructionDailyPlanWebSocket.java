package com.yingda.lkj.socket;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/constructionDailyPlanWebSocket")
@Component
@Slf4j
public class ConstructionDailyPlanWebSocket {

    private Session session;
    private static final CopyOnWriteArraySet<ConstructionDailyPlanWebSocket> constructionDailyPlanWebSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        constructionDailyPlanWebSockets.add(this);
        log.info("【日计划websocket消息】有新的连接, 总数:{}", constructionDailyPlanWebSockets.size());
    }

    @OnClose
    public void onClose() {
        constructionDailyPlanWebSockets.remove(this);
        log.info("【日计划websocket消息】连接断开, 总数:{}", constructionDailyPlanWebSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【日计划websocket消息】收到客户端发来的消息:{}", message);
        sendMessage(message);
    }

    public void sendMessage(String message) {
        for (ConstructionDailyPlanWebSocket constructionDailyPlanWebSocket : constructionDailyPlanWebSockets) {
            log.info("【日计划websocket消息】广播消息, message={}", message);
            try {
                constructionDailyPlanWebSocket.session.getBasicRemote().sendText(JSON.toJSONString(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
