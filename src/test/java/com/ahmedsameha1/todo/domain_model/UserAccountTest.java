package com.ahmedsameha1.todo.domain_model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTest {
    private UserAccount userAccount;

    @BeforeEach
    public void before() {
        userAccount = new UserAccount();
    }

    @Test
    public void testIsEnabled() {
        assertThat(userAccount.isEnabled()).isFalse();
        userAccount.setEnabled(true);
        assertThat(userAccount.isEnabled()).isTrue();
        userAccount.setEnabled(false);
        assertThat(userAccount.isEnabled()).isFalse();
    }

    @Test
    public void testIsAccountNonLocked() {
        assertThat(userAccount.isAccountNonLocked()).isTrue();
        userAccount.setLocked(true);
        assertThat(userAccount.isAccountNonLocked()).isFalse();
        userAccount.setLocked(false);
        assertThat(userAccount.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testIsAccountNonExpired() {
        assertThat(userAccount.isAccountNonExpired()).isTrue();
        userAccount.setExpired(true);
        assertThat(userAccount.isAccountNonExpired()).isFalse();
        userAccount.setExpired(false);
        assertThat(userAccount.isAccountNonExpired()).isTrue();
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertThat(userAccount.isCredentialsNonExpired()).isTrue();
        userAccount.setCredentialsExpired(true);
        assertThat(userAccount.isCredentialsNonExpired()).isFalse();
        userAccount.setCredentialsExpired(false);
        assertThat(userAccount.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void testTodosPopulatedWithEmptyList() {
        assertThat(userAccount.getTodos()).isNotNull();
        assertThat(userAccount.getTodos()).isEmpty();
    }

    @Test
    public void testAuthoritiesPopulatedWithEmptyList() {
        assertThat(userAccount.getAuthorities()).isNotNull();
        assertThat(userAccount.getAuthorities()).isEmpty();
    }

    @Test
    public void testDefaultValueOfGender() {
        assertThat(userAccount.getGender()).isEqualTo(Gender.UNSPECIFIED);
    }
}
