package hello.login.web.fliter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init"); //초기화 로그
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request; // HttpServletRequest으로 다운캐스팅 하기 -> HTTP 요청이 아닌 경우까지 고려해서 만든 거임
        String requestURI = httpRequest.getRequestURI(); // 경로 식별을 위한 URI받기

        String uuid = UUID.randomUUID().toString(); // 요청 식별 uuid

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI); // 요청 로그
            chain.doFilter(request, response); //다음 필터 호출 or 없으면 디스패처 서블릿 호출 (*필수*)
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter init"); //destroy 로그
    }
}
