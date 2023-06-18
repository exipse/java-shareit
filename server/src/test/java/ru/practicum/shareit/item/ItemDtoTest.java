package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> itemDtoJacksonTester;
    @Autowired
    private JacksonTester<ItemFullDto> itemFullDtoJacksonTester;
    @Autowired
    private JacksonTester<ItemShortForRequestDto> itemShortForRequestDtoJacksonTester;


    @Test
    void testItemShortDto() throws Exception {
        ItemShortForRequestDto itemShortForRequestDto = new ItemShortForRequestDto(1L, "Дрель", 1L,
                "Простая дрель", true, 1L);
        JsonContent<ItemShortForRequestDto> result = itemShortForRequestDtoJacksonTester.write(itemShortForRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Instrument",
                "Just Instrument", true, 1L, 1L);
        JsonContent<ItemDto> result = itemDtoJacksonTester.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Instrument");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Just Instrument");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

    @Test
    void testItemFullDto() throws Exception {
        ItemFullDto itemFullDto = ItemFullDto.builder()
                .id(1L)
                .name("Молоток")
                .description("-")
                .available(true)
                .ownerId(1L)
                .build();
        JsonContent<ItemFullDto> result = itemFullDtoJacksonTester.write(itemFullDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("-");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}