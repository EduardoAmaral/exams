package com.eamaral.exams.user.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.infrastructure.repository.jpa.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("should save an user")
    void save() {
        User user = User.builder()
                .id("1")
                .email("a@email.com")
                .name("Adam")
                .surname("S")
                .build();

        repository.save(user);

        assertThat(entityManager.find(UserEntity.class, "1")).isNotNull();
    }

    @Test
    @DisplayName("should not save an user without required fields")
    void save_whenRequiredFieldsIsNull() {
        List<String> validationMessages = List.of("Email is required",
                "Name is required",
                "Surname is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(User.builder().id("1").build()))
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should not save an user that has an invalid email")
    void save_whenEmailIsInvalid() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    final User user = User.builder()
                            .id("1")
                            .email("abc")
                            .name("A")
                            .surname("B")
                            .build();
                    repository.save(user);
                })
                .matches(e -> e.getConstraintViolations().stream()
                        .anyMatch(v -> v.getMessage().equals("Email used is invalid")));
    }

    @Test
    @DisplayName("should retrieve an user when their email exists")
    void findByEmail() {
        final String email = "as@email.com";
        final UserEntity entity = UserEntity.builder()
                .id("1")
                .name("A")
                .surname("S")
                .email(email)
                .picture("https://mypicture.com/profile.jpeg")
                .build();

        entityManager.merge(entity);

        final Optional<User> user = repository.findByEmail(email);

        assertThat(user.orElseThrow())
                .extracting(User::getFullName, User::getEmail, User::getPicture)
                .containsExactly("A S", email, "https://mypicture.com/profile.jpeg");
    }

    @Test
    @DisplayName("should retrieve empty when user is not found by their email")
    void findByEmail_whenEmailDoesNotExist() {
        assertThat(repository.findByEmail("a")).isEmpty();
    }
}