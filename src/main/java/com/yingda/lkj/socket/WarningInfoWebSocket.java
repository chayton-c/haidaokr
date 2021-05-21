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

@ServerEndpoint(value = "/warningInfoWebSocket")
@Component
@Slf4j
public class WarningInfoWebSocket {

    private Session session;
    private static final CopyOnWriteArraySet<WarningInfoWebSocket> warningInfoWebSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        warningInfoWebSockets.add(this);
        log.info("【预警websocket消息】有新的连接, 总数:{}", warningInfoWebSockets.size());
    }

    @OnClose
    public void onClose() {
        warningInfoWebSockets.remove(this);
        log.info("【预警websocket消息】连接断开, 总数:{}", warningInfoWebSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【预警websocket消息】收到客户端发来的消息:{}", message);
        sendMessage(message);
    }

    public void sendMessage(String message) {
        for (WarningInfoWebSocket warningInfoWebSocket: warningInfoWebSockets) {
            log.info("【预警websocket消息】广播消息, message={}", message);
            try {
                warningInfoWebSocket.session.getBasicRemote().sendText(JSON.toJSONString(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
