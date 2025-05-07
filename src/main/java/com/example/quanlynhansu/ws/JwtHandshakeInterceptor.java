package com.example.quanlynhansu.ws;

import com.example.quanlynhansu.services.securityService.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;


// khi người truy cập web bình thường ("./api/**") thì spring security dễ dàng lấy được token từ header.
// nhưng qua webSocket không chạy qua filter bình thường của spring security, mà phải dùng interceptor để xử lý.
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService; // lớp kiểm tra JWT, đã có hoặc sẽ tạo

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            var servlet = servletRequest.getServletRequest();
            String token = servlet.getParameter("token"); // Lấy từ query param
//            if (token.startsWith("Bearer ")) {
//                token = token.substring(7); // bỏ chữ Bearer
                String username = jwtService.extractUserName(token); // trích username từ token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    // Tạo đối tượng Authentication từ username
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, List.of());
                    // Gán Authentication vào attributes của WebSocket session
                    attributes.put("auth", auth);
                    attributes.put("username", username); // gắn vào attributes session
                    return true;
                }
//            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

}
