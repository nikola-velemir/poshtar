package demo.user.repository;

import demo.user.model.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);
}
