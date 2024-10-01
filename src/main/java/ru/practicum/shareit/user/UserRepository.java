package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    User add(User newUser);

    User update(User user);

    Optional<User> getById(long userId);

    void delete(long userId);
}
