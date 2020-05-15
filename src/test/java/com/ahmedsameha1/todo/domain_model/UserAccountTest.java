package com.ahmedsameha1.todo.domain_model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTest {
    private UserAccount userAccount;
    private ObjectMapper objectMapper;

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

    @Test
    public void testSerialization() throws JsonProcessingException {
        userAccount.setUsername(UUID.randomUUID().toString());
        userAccount.setPassword("ffffff3Q");
        userAccount.setFirstName("user2");
        userAccount.setLastName("user2");
        userAccount.setGender(Gender.MALE);
        userAccount.setBirthDay(LocalDate.of(2010, 10, 10));
        userAccount.setEmail("user2@user2.com");
        objectMapper = new ObjectMapper();
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"id\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"creationTime\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"updateTime\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"credentialsNonExpired\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"accountNonLocked\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"accountNonExpired\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"authorities\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"password\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"todos\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"enabled\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"locked\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"expired\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"credentialsExpired\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .doesNotContain("\"version\":");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .contains("\"firstName\"");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .contains("\"lastName\"");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .contains("\"email\"");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .contains("\"gender\"");
        assertThat(objectMapper.writeValueAsString(userAccount))
                .contains("\"birthDay\":\"2010-10-10\"");
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        var json = "{\"id\":\"d309a520-5554-47b9-87f8-a71244c06c6a\","
                + "\"creationTime\":\"1000000000-12-31T23:59:59.999999999Z\","
                + "\"updateTime\":\"1000000000-12-31T23:59:59.999999999Z\","
                + "\"username\":\"user1\",\"password\":\"ffffff3Q\","
                + "\"firstName\":\"user1\",\"lastName\":\"user1\",\"email\":\"user1@user1.com\","
                + "\"birthDay\":\"2010-10-10\",\"gender\":\"MALE\",\"todos\":[],"
                + "\"enabled\":true,\"locked\":true,\"expired\":true,\"credentialsExpired\":true,"
                + "\"version\":30,\"authorities\":[]}";
        var obj = objectMapper.readValue(json, UserAccount.class);
        assertThat(obj.getId()).isNull();
        assertThat(obj.getCreationTime()).isNull();
        assertThat(obj.getUpdateTime()).isNull();
        assertThat(obj.getUsername()).isEqualTo("user1");
        assertThat(obj.getPassword()).isEqualTo("ffffff3Q");
        assertThat(obj.getFirstName()).isEqualTo("user1");
        assertThat(obj.getLastName()).isEqualTo("user1");
        assertThat(obj.getEmail()).isEqualTo("user1@user1.com");
        assertThat(obj.getBirthDay()).isEqualTo(LocalDate.of(2010, 10, 10));
        assertThat(obj.getGender()).isEqualTo(Gender.MALE);
        assertThat(obj.getTodos()).isEmpty();
        assertThat(obj.isEnabled()).isFalse();
        assertThat(obj.isLocked()).isFalse();
        assertThat(obj.isExpired()).isFalse();
        assertThat(obj.isCredentialsExpired()).isFalse();
        assertThat(obj.getVersion()).isEqualTo(0L);
        assertThat(obj.getAuthorities()).isEmpty();
    }
}
