package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item createItemByUser(Item item);

    Item updateItemByUser(int itemId, Item item);

    Optional<Item> getById(int id);

    ArrayList<Item> getAllItems();

    List<Item> seachAll(String text);
}
