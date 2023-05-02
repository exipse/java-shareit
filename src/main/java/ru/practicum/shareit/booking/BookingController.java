package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private int id;
    private Instant start;
    private Instant end;
    private Item item;
    private int booker;
    private Status status;
}
