package me.youngwon.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.User;
import me.youngwon.springbootdeveloper.dto.AddUserRequest;
import me.youngwon.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 1. 패스워드 암호화
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 메서드 추가
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public String generateUniqueNickname(String baseNickname) {
        String nickname = baseNickname;
        int suffix = 1;

        // 이미 존재하는 닉네임이면 뒤에 숫자 붙이기
        while (userRepository.existsByNickname(nickname)) {
            nickname = baseNickname + suffix;
            suffix++;
        }

        return nickname;
    }
}
