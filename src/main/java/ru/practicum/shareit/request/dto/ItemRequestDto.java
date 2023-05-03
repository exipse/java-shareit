package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ItemRequestDto {

    private int id;
    private String description;
    private int requestor;
    private Instant created;
}
