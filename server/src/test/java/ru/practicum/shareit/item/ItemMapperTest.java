package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {

    private final Item item1 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final ItemDto itemDto1 = new ItemDto(1L, "Дрель", "Простая дрель",
            true, 1L, 1L);
    private final ItemShortForRequestDto itemShortForRequestDto = new ItemShortForRequestDto(1L, "Дрель", 1L,
            "Простая дрель", true, 1L);
    private final ItemMapper mapper
            = Mappers.getMapper(ItemMapper.class);

    @Test
    public void toUserModelListTest() {
        Item item = mapper.toItemModel(itemDto1);
        assertEquals(itemDto1.getName(), item.getName());
        assertEquals(itemDto1.getDescription(), item.getDescription());
    }

    @Test
    public void toItemDtoListTest() {
        List<ItemDto> item = mapper.toItemDtoList(List.of(item1));
        assertEquals(item1.getName(), item.get(0).getName());
        assertEquals(1, item.size());
    }

    @Test
    public void toItemDtoListTestNull() {

        List<Item> items = null;
        List<ItemDto> item = mapper.toItemDtoList(items);
        assertNull(item);
    }

    @Test
    public void toUserModelNullListTest() {
        Item item = mapper.toItemModel(null);
        assertNull(item);
    }

    @Test
    public void toUserDtoList() {
        ItemDto itemDto = mapper.toItemDto(item1);
        assertEquals(item1.getName(), itemDto.getName());
        assertEquals(item1.getDescription(), itemDto.getDescription());
    }

    @Test
    public void toUserDtoNullList() {
        ItemDto itemDto = mapper.toItemDto(null);
        assertNull(itemDto);
    }

    @Test
    public void toItemShortForReqDtoTest() {
        ItemShortForRequestDto itemDto = mapper.toItemShortForReqDto(item1);
        assertEquals(item1.getName(), itemDto.getName());
        assertEquals(item1.getOwnerId(), itemDto.getOwnerId());
        assertEquals(item1.getRequestId(), itemDto.getRequestId());
        assertEquals(item1.getDescription(), itemDto.getDescription());
    }

    @Test
    public void toItemShortForReqDtoTestNull() {
        ItemShortForRequestDto itemDto = mapper.toItemShortForReqDto(null);
        assertNull(itemDto);
    }

    @Test
    public void toItemShortForReqDtosTest() {
        List<ItemShortForRequestDto> items = mapper.toItemShortForReqDtos(List.of(item1));

        assertEquals(item1.getName(), items.get(0).getName());
        assertEquals(item1.getDescription(), items.get(0).getDescription());
        assertEquals(item1.getOwnerId(), items.get(0).getOwnerId());
        assertEquals(1, items.size());
    }

    @Test
    public void toItemShortForReqDtosTestNull() {

        List<Item> items = null;
        List<ItemShortForRequestDto> itemsReq = mapper.toItemShortForReqDtos(items);
        assertNull(itemsReq);
    }
}