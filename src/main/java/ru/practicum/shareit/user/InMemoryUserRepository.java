package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Хранилище данных о пользователя с реализацией в ОЗУ.
 */
@Repository
public class InMemoryUserRepository implements UserRepository {
    /**
     * Внутренний счетчик идентификаторов пользователей.
     */
    private long idCounter = 0L;

    /**
     * Собственно хранилище пользователей.
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Используемые email пользователей. Объект для контроля по уникальности.
     */
    private final Map<String, User> emailUsed = new HashMap<>();

    /**
     * Метод для генерации уникальных идентификаторов.
     * @return Новый id пользователя.
     */
    private long getNextId() {
        return ++idCounter;
    }

    /**
     * Метод добавления.
     * @param newUser пользователь для добавления со всеми необходимыми атрибутами.
     * @return Объект добавленного пользователя.
     */
    @Override
    public User add(User newUser) {
        if (emailUsed.containsKey(newUser.getEmail())) {
            throw new ConflictException("Пользователь с email = " + newUser.getEmail() + " уже существует.");
        }

        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        emailUsed.put(newUser.getEmail(), newUser);

        return newUser;
    }

    /**
     * Метод изменения.
     * @param user пользователь для изменения с необходимыми атрибутами.
     * @return Объект измененного пользователя.
     */
    @Override
    public User update(User user) {
        if (emailUsed.containsKey(user.getEmail())
                && !emailUsed.get(user.getEmail()).getId().equals(user.getId())) {
            throw new ConflictException("Пользователь с email = " + user.getEmail() + " уже существует.");
        }
        users.put(user.getId(), user);
        emailUsed.put(user.getEmail(), user);

        return user;
    }

    /**
     * Метод получения данных о пользователе по идентификатору.
     * @param userId идентификатор пользователя.
     * @return Опциональный объект найденного пользователя.
     */
    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    /**
     * Метод удаления пользователя.
     * @param userId идентификатор пользователя, подлежащего удалению.
     */
    @Override
    public void delete(long userId) {
        emailUsed.remove(users.get(userId).getEmail());
        users.remove(userId);
    }
}
