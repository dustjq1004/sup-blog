package me.kimyeonsup.home.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.home.util.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

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

        clientIP = StringUtils.nvl(clientIP, request.getRemoteAddr());
        requestId = StringUtils.nvl(requestId, UUID.randomUUID().toString()); // local 환경에서 구분을 위하여 추가

        MDC.put("clientIP", clientIP);
        MDC.put("requestId", requestId);
        
        // /api로 시작하는 요청에 대해서만 response body 캡처
        ContentCachingResponseWrapper responseWrapper = null;
        if (request.getRequestURI().startsWith("/api")) {
            responseWrapper = new ContentCachingResponseWrapper(response);
            responseWrapper.setCharacterEncoding("UTF-8");
            filterChain.doFilter(request, responseWrapper);
        } else {
            filterChain.doFilter(request, response);
        }

        int status = response.getStatus();
        if (status >= 400) {
            log.error("Request failed with status: {}, clientIP: {}, requestId: {}", status, clientIP, requestId);
        } else {
            log.info("Request completed with status: {}, clientIP: {}, requestId: {}", status, clientIP, requestId);
        }
        
        // 특정 url "/api" 로 시작할 때만 response body 로그 남기기
        if (request.getRequestURI().startsWith("/api") && Objects.nonNull(responseWrapper)) {
            byte[] content = responseWrapper.getContentAsByteArray();
            String responseBody = new String(content, responseWrapper.getCharacterEncoding());
            log.info("Response body: {}", responseBody);
            responseWrapper.copyBodyToResponse();
        }

        MDC.clear();
    }

}
