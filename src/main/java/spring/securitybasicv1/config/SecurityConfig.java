package spring.securitybasicv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터(Config)가 스프링 필터체인에 등록됨
public class SecurityConfig {

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebSecurityConfigurerAdapter가 Spring Security 5.7.0-M2부터 deprecated 됨.
     * component-based security configuration으로의 사용자 전환 격려 위함.
     * 따라서 아래와 같이 bean으로 등록하여 사용.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        //권한 부여
        //authorizeRequests가 deprecated됨에 따라 authorizeHttpRequests 사용 권장
        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/user/**").authenticated()
                    .requestMatchers("/manager/**").hasAnyRole("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .requestMatchers("/admin/**").hasAnyRole("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll();  //이외의 요청은 모두 허용
        });

        http.formLogin(f-> {
            f.loginPage("/loginForm");
            f.loginProcessingUrl("/login");  // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해 준다.
                                             // 컨트롤러의 /login을 생성하지 않아도 된다.
            f.defaultSuccessUrl("/");
        });

        return http.build();
    }
}
