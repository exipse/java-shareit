package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ItemShortForRequestDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String description;
    private Boolean available;
    private Long requestId;

}
