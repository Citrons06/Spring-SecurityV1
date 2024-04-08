package spring.securitybasicv1.config.auth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import spring.securitybasicv1.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * /login 요청이 오면 시큐리티가 낚아채서 로그인 진행
 * 로그인 진행이 완료되면 session을 만들어 줌 (Security ContextHolder에 세션 정보 저장)
 * 들어갈 수 있는 세션 정보(오브젝트): Authentication 타입의 객체
 * Authentication 안에 User 정보가 있어야 함
 * User 오브젝트 타입: UserDetails 타입 객체
 *
 * Security Session => Authentication => UserDetails(PrincipalDetails)
 */
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //일반 로그인할 때 사용하는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth 로그인할 때 사용하는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //해당 User의 권한을 리턴하는 곳
    //ArrayList는 Collection의 자식
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되었는가?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 락되었는가?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //패스워드를 너무 오래 사용하였는가?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화되어 있는가?
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
