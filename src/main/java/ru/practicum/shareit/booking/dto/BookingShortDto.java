package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingShortDto {

    private Long id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}

