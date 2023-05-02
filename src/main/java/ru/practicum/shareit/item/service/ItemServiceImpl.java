package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNoFoundException;
import ru.practicum.shareit.exception.UserNoAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperList;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserStorage userStorage;
    ItemMapper mapper;
    ItemMapperList mapperList;

    @Override
    public ItemDto createItem(ItemDto item, int userId) {

        Optional<User> user = userStorage.get(userId);
        if (user.isPresent()) {
            item.setUserId(userId);
        }
        Item storageItem = itemRepository.createItemByUser(mapper.toItemModel(item));
        log.info("Вещь сохранена");
        return mapper.toItemDto(storageItem);
    }

    @Override
    public ItemDto updateItem(int itemId, int userId, ItemDto item) {
        ItemDto itemInMemory = getById(itemId);
        if (itemInMemory.getUserId() != userId) {
            throw new UserNoAccessException(String.format("Пользователь с id %d не может редактировать вещь с id %d",
                    userId, itemId));
        }
        if (item.getName() != null) {
            itemInMemory.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInMemory.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInMemory.setAvailable(item.getAvailable());
        }
        Item storageItem = itemRepository.updateItemByUser(itemId, mapper.toItemModel(itemInMemory));
        log.info(String.format("Вещь по id = %s обновлена", itemId));
        return mapper.toItemDto(storageItem);
    }

    @Override
    public ItemDto getById(int itemId) {
        Optional<Item> itemFromRepository = itemRepository.getById(itemId);
        if (!itemFromRepository.isPresent()) {
            throw new ItemNoFoundException(String.format("Вещь по id %s не найдена", itemId));

        }
        Item item = itemFromRepository.get();
        log.info(String.format("Вещь по id = %s получена", itemId));
        return mapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUser(int userId) {
        ArrayList<Item> allItems = itemRepository.getAllItems();
        List<Item> userItems = allItems.stream().filter(x -> x.getUserId() == userId).collect(Collectors.toList());
        log.info(String.format("Вещь пользователя id = %s получены", userId));
        return mapperList.toItemDtoList(userItems);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.seachAll(text);
        log.info(String.format("Количество найденных вещей = %d", items.size()));
        return mapperList.toItemDtoList(items);
    }
}
