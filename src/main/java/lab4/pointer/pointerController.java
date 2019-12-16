package lab4.pointer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import lab4.DBUtils;
import lab4.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Controller
public class pointerController
{
    final HazelcastInstance hazelcastInstance;
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    ObjectMapper objectMapper;

    private Set<Train> trainsSet;

    public pointerController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) throws SQLException, ClassNotFoundException
    {
        this.hazelcastInstance = hazelcastInstance;
        this.trainsSet = new HashSet<>();
        DBUtils.connectDB();

        hazelcastInstance.<String>getTopic("newTrain").addMessageListener(message ->
        {
            for (WebSocketSession webSocketSession : webSocketSessions) {
                try
                {
                    trainsSet.clear();
                    trainsSet = DBUtils.readDB();
                    webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(trainsSet)));
                }
                catch (IOException | SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Bean
    WebSocketConfigurer webSocketConfigurer()
    {
        return new WebSocketConfigurer()
        {
            @Override
            public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
            {
                registry.addHandler(new TextWebSocketHandler()
                {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception
                    {
                        webSocketSessions.add(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
                    {
                        webSocketSessions.remove(session);
                    }

                    @Override
                    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
                    {
                        trainsSet.clear();
                        trainsSet = DBUtils.readDB();
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(trainsSet)));
                    }
                }, "/ws");
            }
        };
    }

    @GetMapping("/train")
    public String train() {
        return "trains";
    }
}