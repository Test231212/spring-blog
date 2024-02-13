package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration // 컴퍼넌트 스캔
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignore(){ // 제외 부분, 정적자원 Security filter에서 제외시키기
        return w -> w.ignoring().requestMatchers("/static/**", "/h2-console/**");
    }
    @Bean
    SecurityFilterChain configure(HttpSecurity http)throws Exception{

        http.csrf(c -> c.disable());


        http.authorizeHttpRequests(a -> { //인증이 필요한 부분
            a.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\+d")).permitAll()
                    .requestMatchers("/user/**", "/board/**").authenticated()
                    .anyRequest().permitAll();

        });


        http.formLogin(f -> { // 로그인페이지로 리다이렉트
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/").failureForwardUrl("/loginForm");
        } );

        return http.build();
    }
}
