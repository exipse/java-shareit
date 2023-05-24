package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b from Booking as b where b.booker = ?1 order by b.start desc")
    List<Booking> findALLUserBookings(User user);

    /**
     * querry-current
     */
    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User user, LocalDateTime start,
                                                                               LocalDateTime end);

    /**
     * querry-past
     */
    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime end);

    /**
     * querry-future
     */
    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime start);

    /**
     * querry- + status
     */
    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, Status state);

    /**
     * all owner
     */
    @Query("select b from Booking b join Item i on b.item.id = i.id where i.ownerId =?1 order by b.start DESC")
    List<Booking> findAllByBookerAll(int owner);

    /**
     * querry-current
     */
    @Query("select b from Booking as b join Item as i on b.item.id = i.id   " +
            "where i.ownerId =?1 and b.start <?2 and b.end >?3 " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByOwner(int orderId, LocalDateTime start, LocalDateTime end);

    /**
     * querry-past
     */
    @Query("select b from Booking as b  join Item as i on b.item.id = i.id " +
            "where i.ownerId =?1 and b.end < ?2 " +
            "order by b.start DESC")
    List<Booking> findPastBookingsByOwner(int orderId, LocalDateTime end);

    /**
     * querry-future
     */
    @Query("select b from Booking as b inner join Item as i on b.item.id = i.id " +
            "where i.ownerId =?1  and b.start >?2 " +
            "order by b.start DESC")
    List<Booking> findFutureBookingsByOwner(int orderId, LocalDateTime start);

    /**
     * querry- +owner+ status
     */
    @Query("select b from Booking as b inner join Item as i on b.item.id = i.id " +
            "where i.ownerId =?1 and b.status =?2 " +
            "order by b.start DESC")
    List<Booking> findAllByBookerByStatusOwner(int orderId, Status state);

    List<Booking> findAllByItemAndBookerAndStatusAndEndBefore(Item item, User user, Status state, LocalDateTime time);

    @Query("select b from Booking as b inner join Item as i on b.item.id = i.id " +
            "where i.id =?1 order by b.start DESC")
    List<Booking> findAllBookingItems(int itemId);

}
