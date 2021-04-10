package com.esi.twilio_call;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class TwilioStreamsHandler extends AbstractWebSocketHandler {
    private Map<WebSocketSession,SpeechToTextService> service = new HashMap<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        service.put(session, new SpeechToTextService(
                transcription ->{
                    System.out.println("Transcription : "+ transcription);
                }
        ));


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        service.get(session).send(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        service.get(session).close();
        service.remove(session);
    }


}
