package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}
