package ru.otus.singledbdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.singledbdemo.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
