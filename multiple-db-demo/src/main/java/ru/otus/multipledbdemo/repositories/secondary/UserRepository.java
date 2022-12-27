package ru.otus.multipledbdemo.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.multipledbdemo.domain.secondary.ExternalUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ExternalUser, Long> {
    Optional<ExternalUser> findByLogin(String login);
}
