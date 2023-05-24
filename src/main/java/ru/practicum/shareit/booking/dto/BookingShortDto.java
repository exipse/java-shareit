package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingShortDto {

    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}

