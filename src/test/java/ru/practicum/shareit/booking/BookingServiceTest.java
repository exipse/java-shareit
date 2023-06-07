package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.mapper.ItemFullMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserService userService;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemFullMapper itemFullMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemMapper itemMapper;

    User user1;
    UserDto userDto1;

    Item item1;
    ItemDto itemDto1;
    ItemFullDto itemFullDto1;

    Booking booking1;
    BookingDto bookingDto1;
    BookingRequestDto bookingRequestDto;

    BookingShortDto bookingShortDto1;

    @BeforeEach
    void before() {
        user1 = new User(1L, "user1", "user1@user.com");
        userDto1 = new UserDto(1L, "user1", "user1@user.com");

        item1 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
        itemDto1 = new ItemDto(1L, "Дрель", "Простая дрель", true, 1L, 1L);

        booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(7L))
                .end(LocalDateTime.now().minusHours(5L))
                .item(item1)
                .booker(user1)
                .status(Status.WAITING).build();

        bookingDto1 = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(7L))
                .end(LocalDateTime.now().minusHours(5L))
                .item(itemDto1)
                .booker(userDto1)
                .status(Status.WAITING).build();

        bookingShortDto1 = BookingShortDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(7L))
                .end(LocalDateTime.now().minusHours(5L))
                .bookerId(1L).build();

        bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1L))
                .end(LocalDateTime.now().plusHours(3L))
                .build();

        itemFullDto1 = ItemFullDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .ownerId(1L)
                .build();

        Mockito.when(userMapper.toUserModel(userDto1)).thenReturn(user1);
        Mockito.when(itemMapper.toItemModel(itemDto1)).thenReturn(item1);
        Mockito.when(itemMapper.toItemDto(item1)).thenReturn(itemDto1);
        Mockito.when(itemFullMapper.itemFulltoDto(item1)).thenReturn(itemFullDto1);
        Mockito.when(itemFullMapper.itemFulltoModel(itemFullDto1)).thenReturn(item1);
    }


    @Test
    void createBooksWithNoExistUser() {
        Mockito.when(userMapper.toUserModel(userDto1)).thenReturn(user1);
        when(userStorage.findById(anyLong())).thenThrow(new UserNoFoundException("Пользователя не существует"));
        UserNoFoundException exception = assertThrows(UserNoFoundException.class,
                () -> bookingService.create(2L, bookingRequestDto));
        assertEquals("Пользователя не существует", exception.getMessage());
    }


    @Test
    void createBooksWithNoExistItem() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNoFoundException("Вещи не существует"));
        ItemNoFoundException exception = assertThrows(ItemNoFoundException.class,
                () -> bookingService.create(2L, bookingRequestDto));
        assertEquals("Вещи не существует", exception.getMessage());
    }


    @Test
    void createBooksWithNoCorrectDateTime() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        BookingRequestDto errorDate = BookingRequestDto.builder().
                itemId(2L)
                .start(LocalDateTime.now().minusHours(5L))
                .end(LocalDateTime.now().minusHours(7L))
                .build();
        DataTimeValidateException exception = assertThrows(DataTimeValidateException.class,
                () -> bookingService.create(2L, errorDate));
        assertEquals(String.format("Указано некорректное время бронирования"), exception.getMessage());
    }

    @Test
    void createBooksWhenOwnerItemWantstoBookHisOwnItem() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        when(userService.get(anyLong())).thenReturn(userDto1);
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemFullDto1);

        BookingNoFoundException exception = assertThrows(BookingNoFoundException.class,
                () -> bookingService.create(1L, bookingRequestDto));

        assertEquals(String.format("Владелец вещи не может забронировать свою вещь"), exception.getMessage());
    }

    @Test
    void createBooksButItemNotAvailabletoBook() {

        Item itemNoAvailable
                = new Item(1L, "Дрель", "Простая дрель", false, 2L, 1L);

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        when(userService.get(anyLong())).thenReturn(userDto1);
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemFullDto1);

        Mockito.when(itemFullMapper.itemFulltoModel(itemFullDto1)).thenReturn(itemNoAvailable);

        NoAvailableException exception = assertThrows(NoAvailableException.class,
                () -> bookingService.create(1L, bookingRequestDto));

        assertEquals(String.format("Вещь не доступна для бронирования"), exception.getMessage());
    }

    @Test
    void createBooks() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userService.get(anyLong())).thenReturn(userDto1);
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemFullDto1);
        when(bookingRepository.save(any())).thenReturn(booking1);
        when(bookingMapper.toBookingDto(booking1)).thenReturn(bookingDto1);
        assertEquals(bookingService.create(2L, bookingRequestDto), bookingDto1);
    }


    @Test
    void getInfoByBookingWhenBookingNoExist() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenThrow(new BookingNoFoundException("Бронирования не существует"));

        BookingNoFoundException exception = assertThrows(BookingNoFoundException.class,
                () -> bookingService.getInfoByBook(1L, 2L));
        assertEquals("Бронирования не существует", exception.getMessage());
    }

    @Test
    void getInfoByBookingWhenUserNotOwner() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));

        UserNoFoundException exception = assertThrows(UserNoFoundException.class,
                () -> bookingService.getInfoByBook(1L, 5L));
        assertEquals("Пользователь не является автором бронирования или владельцем",
                exception.getMessage());
    }

    @Test
    void getInfoByBook() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.checkInfoByBook(anyLong(), anyLong())).thenReturn(Optional.of(booking1));
        when(bookingMapper.toBookingDto(booking1)).thenReturn(bookingDto1);

        assertEquals(bookingDto1, bookingService.getInfoByBook(1L, 5L));
    }


    @Test
    void confirmOrRejectRequestWhenUserNotOwner() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.checkInfoByBook(anyLong(), anyLong())).thenReturn(Optional.of(booking1));
        when(bookingMapper.toBookingDto(booking1)).thenReturn(bookingDto1);

        Long userId = 4L;
        UserNoFoundException exception = assertThrows(UserNoFoundException.class,
                () -> bookingService.confirmOrRejectRequest(userId, 5L, true));
        assertEquals(String.format("Пользователь id = %s не является владельцем вещи которую бронируют", userId),
                exception.getMessage());
    }

    @Test
    void rejectRequest() {

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.checkInfoByBook(anyLong(), anyLong())).thenReturn(Optional.of(booking1));
        when(bookingMapper.toBookingDto(booking1)).thenReturn(bookingDto1);

        Long userId = 1L;
        BookingDto reject = bookingService.confirmOrRejectRequest(userId, 1L, false);
        assertEquals(reject.getStatus().toString(), "REJECTED");
    }

    @Test
    void confirmRequest() {

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.checkInfoByBook(anyLong(), anyLong())).thenReturn(Optional.of(booking1));
        when(bookingMapper.toBookingDto(booking1)).thenReturn(bookingDto1);

        Long userId = 1L;
        BookingDto reject = bookingService.confirmOrRejectRequest(userId, 1L, true);
        assertEquals(reject.getStatus().toString(), "APPROVED");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ALL",
            "CURRENT",
            "PAST",
            "FUTURE",
            "WAITING",
            "REJECTED"
    })
    void getAllBooksByUser(String state) {
        when(userService.get(anyLong())).thenReturn(userDto1);
        when(bookingRepository.findALLUserBookings(any(), any())).thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(any(), any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(any(), any(), any()))
                .thenReturn(List.of(booking1));
        when((bookingMapper.toBookingListDto(List.of(booking1)))).thenReturn(List.of(bookingDto1));


        List<BookingDto> bookings = bookingService.getAllBooksByUser(1L, state, 0, 10);
        assertEquals(bookings.get(0).getId(), 1L);

    }


    @ParameterizedTest
    @CsvSource(value = {
            "ALL",
            "CURRENT",
            "PAST",
            "FUTURE",
            "WAITING",
            "REJECTED",
    })
    void getAllBooksByOwner(String state) {
        when(userService.get(anyLong())).thenReturn(userDto1);
        when(bookingRepository.findAllByBookerAll(anyLong(), any())).thenReturn(List.of(booking1));
        when(bookingRepository.findCurrentBookingsByOwner(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findPastBookingsByOwner(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findFutureBookingsByOwner(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        when(bookingRepository.findAllByBookerByStatusOwner(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));


        when((bookingMapper.toBookingListDto(List.of(booking1)))).thenReturn(List.of(bookingDto1));


        List<BookingDto> bookings = bookingService.getAllBooksByOwner(1L, state, 0, 10);
        assertEquals(bookings.get(0).getId(), 1L);

    }

    @Test
    void getUnsupportedState() {
        when(userService.get(anyLong())).thenReturn(userDto1);
        UnsupportedException exception = assertThrows(UnsupportedException.class,
                () -> bookingService.getAllBooksByOwner(1L, "wrong", 0, 10));

        assertEquals("Unknown state: wrong",
                exception.getMessage());
    }


}