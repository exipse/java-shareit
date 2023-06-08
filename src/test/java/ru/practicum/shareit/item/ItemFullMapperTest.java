package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.mapper.ItemFullMapper;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemFullMapperTest {

    Item item1 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    ItemFullDto itemFullDto1 = ItemFullDto.builder()
            .id(1L)
            .name("Дрель")
            .description("Простая дрель")
            .available(true)
            .ownerId(1L)
            .build();
    private final ItemFullMapper mapper
            = Mappers.getMapper(ItemFullMapper.class);

    @Test
    public void itemFulltoDtoTest() {
        ItemFullDto itemFullDto = mapper.itemFulltoDto(item1);
        assertEquals(item1.getName(), itemFullDto.getName());
        assertEquals(item1.getDescription(), itemFullDto.getDescription());
        assertEquals(item1.getOwnerId(), itemFullDto.getOwnerId());
    }

    @Test
    public void itemFulltoDtoTestNull() {
        ItemFullDto itemFullDto = mapper.itemFulltoDto(null);
        assertNull(itemFullDto);
    }

    @Test
    public void itemFulltoModelTest() {
        Item item = mapper.itemFulltoModel(itemFullDto1);
        assertEquals(itemFullDto1.getName(), item.getName());
        assertEquals(itemFullDto1.getDescription(), item.getDescription());
        assertEquals(itemFullDto1.getOwnerId(), item.getOwnerId());
    }

    @Test
    public void itemFulltoModelTestNull() {
        Item item = mapper.itemFulltoModel(null);
        assertNull(item);
    }

}