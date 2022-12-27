package ru.otus.multipledbdemo.services;

import org.springframework.stereotype.Component;
import ru.otus.multipledbdemo.domain.primary.User;
import ru.otus.multipledbdemo.domain.secondary.ExternalUser;

@Component
public class UsersMapper {
    public ExternalUser userToExternal(User user) {
        return new ExternalUser(user.getId(), user.getName(), user.getLogin());
    }

    public User externalUserToUser(ExternalUser user) {
        return new User(user.getId(), user.getName(), user.getLogin());
    }

    public void fillUserFromExternalUser(User user, ExternalUser externalUser) {
        user.setId(externalUser.getId());
        user.setNameAndLogin(externalUser.getName(), externalUser.getLogin());
    }
}
