package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserStorage userStorage;
    private User user;
    private Item item;

    @BeforeEach
    void before() {
        user = User.builder().name("user1").email("user1@user.com").build();
        item = Item.builder().name("Instrument").description("Just Instrument").available(true)
                .requestId(1L).ownerId(1L).build();
        userStorage.save(user);
        itemRepository.save(item);
    }

    @Test
    void findAllByOwnerIdOrderById() {

        List<Item> itemList = itemRepository.findAllByOwnerIdOrderById(1L);
        assertEquals(1, itemList.size());
    }

    @Test
    void seachAll() {

        List<Item> itemList =
                itemRepository.seachAll("s");
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
    }

    @Test
    void findAllByRequestId() {
        List<Item> itemList =
                itemRepository.findAllByRequestId(1L);
        assertEquals(1, itemList.size());
    }
}