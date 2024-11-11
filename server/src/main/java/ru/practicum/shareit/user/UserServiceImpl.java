package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * Сервис работы с пользователями.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Преобразователь данных ввода-вывода.
     */
    private final UserMapperImpl userMapper;

    /**
     * Метод добавления пользователя.
     *
     * @param userCreateDto входные данные по пользователю для добавления.
     * @return Выходные данные по добавленному пользователю.
     */
    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.toUserOnCreate(userCreateDto);
        user = userRepository.save(user);

        return userMapper.toUserDto(user);
    }

    /**
     * Метод изменения пользователя.
     *
     * @param userUpdateDto входные данные по пользователю для изменения.
     * @return Выходные данные по измененному пользователю.
     */
    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        User userForUpdate = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден по id = " + userUpdateDto.getId()));

        userMapper.toUserOnUpdate(userForUpdate, userUpdateDto);
        userForUpdate = userRepository.save(userForUpdate);

        return userMapper.toUserDto(userForUpdate);
    }

    /**
     * Метод получения данных по пользователю.
     *
     * @param userId идентификатор пользователя.
     * @return Выходные данные о пользователе.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден по userId = " + userId));

        return userMapper.toUserDto(user);
    }

    /**
     * Метод удаления пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
