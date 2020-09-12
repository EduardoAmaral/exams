package com.eamaral.exams.user.domain.port;

import com.eamaral.exams.user.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);
}
