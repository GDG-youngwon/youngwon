package me.youngwon.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.User;
import me.youngwon.springbootdeveloper.repository.UserRepository;
import me.youngwon.springbootdeveloper.service.UserService;   // 🔥 추가
import org.springframework.stereotype.Service;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("🔍 OAuth2 attributes: " + oAuth2User.getAttributes());

        saveOrUpdate(oAuth2User);
        return oAuth2User;
    }

    // 유저가 있으면 닉네임 업데이트, 없으면 새로운 닉네임 생성해서 저장
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String rawName = (String) attributes.get("name");

        // 기본 닉네임 생성 (공백 제거)
        String baseNickname = (rawName != null && !rawName.isBlank())
                ? rawName.replaceAll("\\s+", "")
                : email.split("@")[0];

        // 중복 있으면 뒤에 숫자 붙임
        String uniqueNickname = userService.generateUniqueNickname(baseNickname);

        // 존재하면 업데이트, 없으면 새로 저장
        User user = userRepository.findByEmail(email)
                .map(existing -> existing.update(uniqueNickname)) // 닉네임 갱신
                .orElse(User.builder()
                        .email(email)
                        .nickname(uniqueNickname)
                        .build()
                );

        return userRepository.save(user);
    }
}
