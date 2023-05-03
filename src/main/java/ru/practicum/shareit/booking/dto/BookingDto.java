package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;

@Data
public class BookingDto {

    private int id;
    private Instant start;
    private Instant end;
    private Item item;
    private int booker;
    private Status status;
}
