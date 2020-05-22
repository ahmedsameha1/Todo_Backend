package com.ahmedsameha1.todo.service;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.email_verification.NeedEmailVerificationToken;
import com.ahmedsameha1.todo.exception.UserExistsException;
import com.ahmedsameha1.todo.repository.EmailVerificationTokenRepository;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceUnitTest {
    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserAccount userAccount;

    @InjectMocks
    private UserAccountService userAccountService = new UserAccountServiceImpl();

    @Nested
    @DisplayName("RegisterUserAccount tests")
    class RegisterUserAccount {
        @Test
        @DisplayName("Should fail because there is another UserAccount with the same username")
        public void test1() {
            when(userAccount.getUsername()).thenReturn("ffffff");
            when(userAccountRepository.findByUsername(anyString())).thenReturn(userAccount);
            assertThatThrownBy(() -> userAccountService
            .registerUserAccount(userAccount, new MockHttpServletRequest()))
                    .isInstanceOf(UserExistsException.class);
            verify(userAccount).getUsername();
            verify(userAccountRepository).findByUsername(anyString());
        }

        @Test
        @DisplayName("Should fail the UserAccount is null")
        public void test2() {
            assertThatThrownBy(() -> userAccountService
                    .registerUserAccount(null, new MockHttpServletRequest()))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should fail because UserAccount has null username")
        public void test3() {
            when(userAccountRepository.findByUsername(isNull())).thenThrow(IllegalArgumentException.class);
            assertThatThrownBy(() -> userAccountService
                    .registerUserAccount(userAccount, new MockHttpServletRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
            verify(userAccount).getUsername();
            verify(userAccountRepository).findByUsername(isNull());
        }

        @Test
        @DisplayName("Test calling the needed methods")
        public void test5() {
            when(userAccount.getUsername()).thenReturn("fff");
            when(userAccountRepository.findByUsername(anyString())).thenReturn(null);
            when(userAccountRepository.save(userAccount)).thenReturn(userAccount);
            when(userAccount.getPassword()).thenReturn("ffff");
            when(bCryptPasswordEncoder.encode(anyString())).thenReturn("ffff");
            var returnedUserAccount = userAccountService.registerUserAccount(userAccount, new MockHttpServletRequest());
            assertThat(returnedUserAccount).isEqualTo(userAccount);
            verify(userAccount).setPassword(anyString());
            verify(userAccountRepository).save(userAccount);
            verify(applicationEventPublisher).publishEvent(any(NeedEmailVerificationToken.class));
        }
    }
}
