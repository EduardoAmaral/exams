package com.eamaral.exams.user.infrastructure.repository;

import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import com.eamaral.exams.user.infrastructure.repository.jpa.entity.UserEntity;
import com.eamaral.exams.user.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository implements UserRepositoryPort {

    private final UserJpaRepository repository;

    public UserRepository(UserJpaRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.saveAndFlush(UserEntity.from(user))
                .toUser();
    }

    public Optional<User> findByEmail(String email) {
        final Optional<UserEntity> entity = repository.findByEmail(email);

        return entity.map(UserEntity::toUser);
    }
}
