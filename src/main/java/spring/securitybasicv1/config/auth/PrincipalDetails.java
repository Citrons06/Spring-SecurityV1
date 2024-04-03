package spring.securitybasicv1.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.securitybasicv1.model.User;

import java.util.Collection;

/**
 * /login 요청이 오면 시큐리티가 낚아채서 로그인 진행
 * 로그인 진행이 완료되면 session을 만들어 줌 (Security ContextHolder에 세션 정보 저장)
 * 들어갈 수 있는 세션 정보(오브젝트): Authentication 타입의 객체
 * Authentication 안에 User 정보가 있어야 함
 * User 오브젝트 타입: UserDetails 타입 객체
 *
 * Security Session => Authentication => UserDetails(PrincipalDetails)
 */
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
