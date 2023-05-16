package ru.practicum.shareit.user.storage.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.UserNoFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {

    private final Map<Integer, User> storage = new HashMap<>();
    private final Set<String> emails = new HashSet();
    private int counter = 0;


    @Override
    public User createUser(User user) {
        if (validateEmail(user.getEmail())) {
            throw new EmailExistException(String.format("Пользователь с email %s уже существует", user.getEmail()));
        }
        user.setId(++counter);
        storage.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User userInMemory = storage.get(user.getId());
        if (!(userInMemory.getEmail().equals(user.getEmail())) &&
                validateEmail(user.getEmail())) {
            throw new EmailExistException(String.format("Пользователь с email %s уже существует", user.getEmail()));
        }
        if (!(user.getName() == null)) {
            userInMemory.setName(user.getName());
        }
        if (!(user.getEmail() == null)) {
            deleteUserEmail(userInMemory.getId());
            userInMemory.setEmail(user.getEmail());

        }
        storage.put(user.getId(), userInMemory);
        emails.add(userInMemory.getEmail());
        return userInMemory;
    }

    @Override
    public Optional<User> get(int id) {
        if (!storage.containsKey(id)) {
            throw new UserNoFoundException(String.format("Пользователя с id %s не найдено", id));
        }
        return Optional.of(storage.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int userId) {
        deleteUserEmail(userId);
        storage.remove(userId);
    }


    private boolean validateEmail(String email) {
        return ifExistEmail(email);
    }

    private void deleteUserEmail(int userId) {
        emails.remove(storage.get(userId).getEmail());
    }

    private boolean ifExistEmail(String email) {
        boolean flag = false;
        if (emails.contains(email)) {
            flag = true;
        }
        return flag;
    }


}
