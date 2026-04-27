package hello.login.web.fliter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {
    //init(), destroy()는 자바파일에 default로 설정되어 있어 구현하지 않아도 됌

    //인증 체크 로직 실행하지 않을 URL 등록
    private static final String[] whitelist = {"/", "/member/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);
            if (isLoginCheckPath(requestURI)) { // 화이트리스트에 없으면 긍정으로 받아서 로직 실행
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false); // 요청에서 세션 받아오기 -> false 값 주고 생성금지
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { // 세션쿠키가 없거나, 세션쿠키 속성에 정보가 없으면 redirect

                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI); // 로그인 페이지로 갔다가 로그인 완료 후 원래 있던 페이지로 다시 이동하게끔 만듦
                    return;
                }
            }

            chain.doFilter(request, response); // 화이트 리스트에 없으면 바로 이 로직임
        } catch (Exception e) {
            throw e; // 예외를 로깅 가능하지만, 톰캣까지 예외를 보내줘야 함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI); // 화이트리스트와 파라미터를 비교한 후 부정으로 반환
    }

}
