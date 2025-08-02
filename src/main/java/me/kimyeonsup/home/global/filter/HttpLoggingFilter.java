package me.kimyeonsup.home.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {

        String clientIP = request.getHeader("X-Forwarded-For");
        String requestId = request.getHeader("X-Request-Id");

        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = request.getRemoteAddr();
        }

        MDC.put("clientIP", clientIP);
        MDC.put("requestId", requestId);
        filterChain.doFilter(request, response);

        int status = response.getStatus();
        if (status >= 400) {
            log.error("Request failed with status: {}, clientIP: {}, requestId: {}", status, clientIP, requestId);
        } else {
            log.info("Request completed with status: {}, clientIP: {}, requestId: {}", status, clientIP, requestId);
        }
        MDC.clear();
    }

}
