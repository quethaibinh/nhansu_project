package com.example.quanlynhansu.configs;

import com.example.quanlynhansu.ws.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // client sẽ subscribe vào đây
        config.setUserDestinationPrefix("/user"); // để gửi riêng cho từng user
        config.setApplicationDestinationPrefixes("/app"); // Định nghĩa prefix cho các message client gửi đến server. (@MessageMapping)
        // VD: client gửi message đến /app/sendMessage, thì Spring sẽ tìm đến hàm controller có @MessageMapping("/sendMessage").
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Client sẽ kết nối WebSocket vào đường dẫn /ws
                .addInterceptors(jwtHandshakeInterceptor) // Gắn JwtHandshakeInterceptor vào giai đoạn bắt tay WebSocket.
                                                          // Mục đích: kiểm tra JWT token để xác thực người dùng trước khi cho phép kết nối WebSocket.
                .setAllowedOriginPatterns("*"); // Cho phép CORS từ bất kỳ domain nào.
//                .withSockJS() // Kích hoạt SockJS – một thư viện fallback. nếu dùng thì phải gửi token qua url, gửi qua header sẽ lỗi.
//                .setSuppressCors(true);
//        Nếu có .withSockJS() → dùng http://... khi khởi tạo với SockJS(...).
//        Nếu không có SockJS → dùng ws://... hoặc wss://... nếu là HTTPS.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && accessor.getCommand() != null) {
                    Authentication auth = (Authentication) accessor.getSessionAttributes().get("auth");

                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth); // cái này giúp lấy Principal trong controller
                    }
                }
                return message;
            }
        });
    }

}


//Tóm tắt luồng hoạt động
    //Client kết nối đến /ws qua SockJS.
    //JwtHandshakeInterceptor chạy trước → kiểm tra token → nếu hợp lệ thì cho kết nối.
    //Client gửi message đến server với prefix /app/sendMessage.
    //Server xử lý message ở method có @MessageMapping("/sendMessage").
    //Server đẩy lại message cho những client đang subscribe vào /topic/....