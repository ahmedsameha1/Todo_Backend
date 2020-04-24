package com.ahmedsameha1.todo.domain_model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserAccountTest {
    private UserAccount userAccount;

    @BeforeEach
    public void before() {
        userAccount = new UserAccount();
    }

    @Test
    public void testIsEnabled() {
        Assertions.assertThat(userAccount.isEnabled()).isFalse();
        userAccount.setEnabled(true);
        Assertions.assertThat(userAccount.isEnabled()).isTrue();
        userAccount.setEnabled(false);
        Assertions.assertThat(userAccount.isEnabled()).isFalse();
    }

    @Test
    public void testIsAccountNonLocked() {
        Assertions.assertThat(userAccount.isAccountNonLocked()).isTrue();
        userAccount.setLocked(true);
        Assertions.assertThat(userAccount.isAccountNonLocked()).isFalse();
        userAccount.setLocked(false);
        Assertions.assertThat(userAccount.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testIsAccountNonExpired() {
        Assertions.assertThat(userAccount.isAccountNonExpired()).isTrue();
        userAccount.setExpired(true);
        Assertions.assertThat(userAccount.isAccountNonExpired()).isFalse();
        userAccount.setExpired(false);
        Assertions.assertThat(userAccount.isAccountNonExpired()).isTrue();
    }

    @Test
    public void testIsCredentialsNonExpired() {
        Assertions.assertThat(userAccount.isCredentialsNonExpired()).isTrue();
        userAccount.setCredentialsExpired(true);
        Assertions.assertThat(userAccount.isCredentialsNonExpired()).isFalse();
        userAccount.setCredentialsExpired(false);
        Assertions.assertThat(userAccount.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void testTodosPopulatedWithEmptyList() {
        Assertions.assertThat(userAccount.getTodos()).isNotNull();
        Assertions.assertThat(userAccount.getTodos()).isEmpty();
    }

    @Test
    public void testAuthoritiesPopulatedWithEmptyList() {
        Assertions.assertThat(userAccount.getAuthorities()).isNotNull();
        Assertions.assertThat(userAccount.getAuthorities()).isEmpty();
    }
}
