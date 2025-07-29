package me.kimyeonsup.home.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String clientIP = request.getHeader("X-Forwarded-For");
        String requestId = request.getHeader("X-Request-Id");
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = request.getRemoteAddr();
        }
        MDC.put("clientIP", clientIP);
        MDC.put("requestId", requestId);

        // 1. 요청 정보 로깅 (Body 제외)
        String url = request.getRequestURL().toString();
        String params = getParams(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("[REQ] url={}, params={}, userAgent={}", url, params, userAgent);

        // 필터 체인 실행 (컨트롤러까지 요청 전달)
        filterChain.doFilter(request, response);

        // 2. 응답 정보 로깅 (Body 출력 포함)
        int status = response.getStatus();
        // 응답 body는 ContentCachingResponseWrapper 없이 바로 읽을 수 없음(참고)
    }

    // 파라미터를 문자열로 출력하는 메서드
    private String getParams(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = req.getParameter(name);
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(name).append("=").append(value);
        }
        return sb.toString();
    }

    @Override
    public void destroy() {
        MDC.remove("clientIP");
        Filter.super.destroy();
    }
}
