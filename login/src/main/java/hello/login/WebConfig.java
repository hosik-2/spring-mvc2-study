package hello.login;

import hello.login.web.argumentResolver.LoginMemberArgumentResolver;
import hello.login.web.fliter.LogFilter;
import hello.login.web.fliter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration //설정 파일
public class WebConfig implements WebMvcConfigurer { // interceptor 사용하려면 설정파일에 WebMvcConfigurer 등록


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver()); // 리졸버 추가
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // 등록할 인터셉터 구현체 등록
                .order(1) // 순서
                .addPathPatterns("/**") //필터랑은 다름 /* 하고 *하나 더 (/* 하위에 모든 확장자? 파일들 /*.png 하면 png만 )
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 화이트리스트임

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**",
                        "/*.ico", "/error");
    }

//    @Bean
    public FilterRegistrationBean logFilter() { //스프링 부트가 was를 들고 띄우는데 얘가 was를 띄울 때 필터를 같이 가져감
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //만든 로그 객체 등록
        filterRegistrationBean.setOrder(1); // 순서 설정, 낮으면 낮은 필터부터 동작함
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 URL 대상 설정

        return filterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // 로그 필터 다음으로 동작
        filterRegistrationBean.addUrlPatterns("/*"); // 화이트리스트를 직접 등록했기 때문에 전체 등록

        return filterRegistrationBean;
    }
}
