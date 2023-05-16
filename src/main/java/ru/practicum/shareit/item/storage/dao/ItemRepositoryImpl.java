package ru.practicum.shareit.item.storage.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, Item> usersItems = new HashMap<>();
    private static int counter = 0;

    @Override
    public Item createItemByUser(Item item) {
        item.setId(++counter);
        usersItems.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItemByUser(int itemId, Item item) {
        usersItems.put(itemId, item);
        return usersItems.get(itemId);
    }

    @Override
    public Optional<Item> getById(int id) {
        return Optional.ofNullable(usersItems.get(id));
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(usersItems.values());
    }

    @Override
    public List<Item> seachAll(String text) {
        List<Item> defenitionList = new ArrayList<>();
        List<Item> items = new ArrayList<>(usersItems.values());
        if (!text.isEmpty()) {
            for (Item item : items) {
                if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable().equals(true)) {
                    defenitionList.add(item);
                }
            }
        }
        return defenitionList;
    }
}
