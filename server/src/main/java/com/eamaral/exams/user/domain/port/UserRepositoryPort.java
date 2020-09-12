package com.eamaral.exams.user.domain.port;

import com.eamaral.exams.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findAllByIds(Set<String> userIds);
}
