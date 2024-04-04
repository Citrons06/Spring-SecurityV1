package spring.securitybasicv1.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.securitybasicv1.model.User;
import spring.securitybasicv1.repository.UserRepository;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/login")으로 설정해 두었기 때문에
 * /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는
 * loadUserByUsername 함수가 실행된다.
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
