package com.ahmedsameha1.todo.security;

import com.ahmedsameha1.todo.ConfigurationTest;
import com.ahmedsameha1.todo.domain_model.Gender;
import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ConfigurationTest.class)
public class UserDetailsServiceImplTest {
    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Should fail because there is no UserAccount with this username")
    public void test1() {
        when(userAccountRepository.findByUsername(anyString())).thenReturn(null);
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(anyString()))
                .isInstanceOf(UsernameNotFoundException.class);
        verify(userAccountRepository).findByUsername(anyString());
    }

    @Test
    @DisplayName("Test that loadUserByUsername returns the correct object")
    public void test2() {
        var userAccount1 = new UserAccount();
        userAccount1.setUsername(UUID.randomUUID().toString());
        userAccount1.setPassword("ffffff3Q");
        userAccount1.setFirstName("user2");
        userAccount1.setLastName("user2");
        userAccount1.setGender(Gender.MALE);
        userAccount1.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount1.setEmail("user2@user2.com");
        var userAccount2 = new UserAccount();
        userAccount2.setUsername(UUID.randomUUID().toString());
        userAccount2.setPassword("ffffff3Q");
        userAccount2.setFirstName("user2");
        userAccount2.setLastName("user2");
        userAccount2.setGender(Gender.MALE);
        userAccount2.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount2.setEmail("user2@user2.com");
        when(userAccountRepository.findByUsername("user1")).thenReturn(userAccount1);
        when(userAccountRepository.findByUsername("user2")).thenReturn(userAccount2);
        assertThat(userDetailsService.loadUserByUsername("user1")).isEqualTo(userAccount1);
        assertThat(userDetailsService.loadUserByUsername("user1")).isNotEqualTo(userAccount2);
        assertThat(userAccountRepository.findByUsername("user2")).isEqualTo(userAccount2);
        assertThat(userAccountRepository.findByUsername("user2")).isNotEqualTo(userAccount1);
        verify(userAccountRepository, times(2)).findByUsername("user1");
        verify(userAccountRepository, times(2)).findByUsername("user2");
    }
}
