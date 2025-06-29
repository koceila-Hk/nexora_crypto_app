//package com.nexora.nexora_crypto_api.repository;
//
//import com.nexora.nexora_crypto_api.model.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//@ActiveProfiles("test")
//@SpringBootTest
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = User.builder()
//                .pseudonym("kouss")
//                .email("koko@gmail.com")
//                .password("vldjsvlkdns")
//                .verificationCode("12345")
//                .enabled(true)
//                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
//                .build();
//
//        userRepository.save(user);
//    }
//
//    @AfterEach
//    void tearDown() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void findByEmail() {
//        Optional<User> _user = userRepository.findByEmail("koko@gmail.com");
//        assertThat(_user).isNotNull();
//        assertThat(_user.get().getPseudonym()).isEqualTo("kouss");
//    }
//
//    @Test
//    void findByVerificationCode() {
//        Optional<User> _user = userRepository.findByVerificationCode("12345");
//        assertThat(_user).isPresent();
//        assertThat(_user.get().getPseudonym()).isEqualTo("kouss");
//    }
//}