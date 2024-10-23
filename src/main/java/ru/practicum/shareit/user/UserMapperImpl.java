package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * Преобразователь данных ввода-вывода по пользователям.
 */
@Component
public class UserMapperImpl implements UserMapper {
    /**
     * Метод преобразования выходных данных по пользователю.
     *
     * @param user пользователь для вывода.
     * @return Объект для вывода данных.
     */
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Метод использования входных данных при создании пользователя.
     *
     * @param userCreateDto объект с данными для создания;
     * @return Объект для вывода данных.
     */
    public User toUserOnCreate(UserCreateDto userCreateDto) {
        return User.builder()
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .build();
    }

    /**
     * Метод использования входных данных при изменении пользователя.
     *
     * @param user          описание пользователя для изменения;
     * @param userUpdateDto объект с данными для изменения.
     */
    public void toUserOnUpdate(User user, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
    }
}
