package com.eamaral.exams.user.infrastructure.repository;

import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import com.eamaral.exams.user.infrastructure.repository.jpa.UserJpaRepository;
import com.eamaral.exams.user.infrastructure.repository.jpa.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

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

    @Override
    public List<User> findAllByIds(Set<String> userIds) {
        return repository.findAllByIdIn(userIds).stream()
                .map(UserEntity::toUser)
                .collect(toList());
    }
}
