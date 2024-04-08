package spring.securitybasicv1.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import spring.securitybasicv1.config.auth.PrincipalDetails;
import spring.securitybasicv1.config.oauth.provider.FacebookUserInfo;
import spring.securitybasicv1.config.oauth.provider.GoogleUserInfo;
import spring.securitybasicv1.config.oauth.provider.NaverUserInfo;
import spring.securitybasicv1.config.oauth.provider.OAuth2UserInfo;
import spring.securitybasicv1.model.User;
import spring.securitybasicv1.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    //엑세스 토큰으로 사용자 프로필 정보를 받은 것이 userRequest 로 리턴 된다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //registrationId 로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info("getClientRegistration={}", userRequest.getClientRegistration());
        log.info("getAccessToken={}", userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리가 받아 줌) -> AccessToken 요청
        //위 까지가 userRequest 정보
        //userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받음
        log.info("getAttributes={}", oAuth2User.getAttributes());


        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("구글, 페이스북, 네이버 로그인만 지원합니다.");
        }

        String provider = oAuth2UserInfo.getProvider();  //google, facebook, naver ...
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;  //google_107440144588213101280
        String password = passwordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        //첫 로그인이면 자동으로 회원가입
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            log.info("최초로 OAuth 로그인하셨습니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            log.info("로그인을 한 적이 있습니다. 자동 회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}