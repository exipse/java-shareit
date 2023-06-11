package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingShortMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNoFoundException;
import ru.practicum.shareit.exception.NoAvailableException;
import ru.practicum.shareit.exception.UserNoAccessException;
import ru.practicum.shareit.exception.UserNoFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommetMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.mapper.ItemFullMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private ItemFullMapper itemFullMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommetMapper commetMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingShortMapper bookingShortMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item1;
    private ItemDto itemDto1;
    private ItemFullDto itemFullDto1;
    private ItemFullDto finalFull;

    private User user1;
    private UserDto userDto1;
    private Comment comment1;
    private List<Comment> commentList;
    private CommentDto commentDto1;
    private List<CommentDto> commentListDto;

    private Booking booking1;
    private Booking booking2;
    private BookingShortDto bookingShortDto1;
    private BookingShortDto bookingShortDto2;
    private List<Booking> bookingList;

    @BeforeEach
    void before() {
        userDto1 = new UserDto(1L, "user1", "user1@user.com");
        user1 = new User(1L, "user1", "user1@user.com");

        itemDto1 = new ItemDto(1L, "Дрель", "Простая дрель",
                true, 1L, 1L);
        item1 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);

        itemFullDto1 = ItemFullDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .ownerId(1L)
                .build();

        comment1 = new Comment(1L, "коммент", item1, user1, LocalDateTime.now());
        commentList = List.of(comment1);

        commentDto1 = new CommentDto(1L, "коммент", itemDto1, "user1", LocalDateTime.now());
        commentListDto = List.of(commentDto1);

        booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(7L))
                .end(LocalDateTime.now().minusHours(5L))
                .item(item1)
                .booker(user1)
                .status(Status.WAITING).build();

        booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(5L))
                .end(LocalDateTime.now().plusHours(7L))
                .item(item1)
                .booker(user1)
                .status(Status.WAITING).build();

        bookingShortDto1 = BookingShortDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(7L))
                .end(LocalDateTime.now().minusHours(5L))
                .bookerId(1L).build();

        bookingShortDto2 = BookingShortDto.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(5L))
                .end(LocalDateTime.now().plusHours(7L))
                .bookerId(1L).build();

        bookingList = List.of(booking1, booking2);

        finalFull = ItemFullDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .ownerId(1L)
                .lastBooking(bookingShortDto1)
                .nextBooking(bookingShortDto2)
                .comments(commentListDto)
                .build();

        Mockito.when(itemMapper.toItemModel(itemDto1)).thenReturn(item1);
        Mockito.when(itemMapper.toItemDto(item1)).thenReturn(itemDto1);
        Mockito.when(itemFullMapper.itemFulltoDto(item1)).thenReturn(itemFullDto1);
    }

    @Test
    void createItem() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user1));
        when(itemRepository.save(item1)).thenReturn(item1);

        assertEquals(itemService.createItem(itemDto1, 1L), itemDto1);
    }

    @Test
    void createItemWhithNoExistUser() {
        when(userStorage.findById(2L)).thenReturn(Optional.empty());
        Exception e = assertThrows(UserNoFoundException.class,
                () -> itemService.createItem(itemDto1, 1L));

        assertEquals("Пользователя не существует", e.getMessage());
    }

    @Test
    void updateItemByOwnerUser() {
        ItemDto update = new ItemDto(1L, "Кружка", "Просто кружка дрель",
                true, 1L, 1L);
        Item updateModel = new Item(1L, "Кружка", "Просто кружка дрель",
                true, 1L, 1L);

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item1));
        when(itemRepository.save(item1)).thenReturn(updateModel);
        when(itemMapper.toItemDto(updateModel)).thenReturn(update);
        assertEquals(itemService.updateItem(1L, 1L, update), update);
    }

    @Test
    void updateItemByUserNoOwner() {
        Mockito.when(itemRepository.findById(anyLong()))
                .thenThrow(new UserNoAccessException("Пользователь не может редактировать вещь"));
        UserNoAccessException exception = assertThrows(UserNoAccessException.class, () ->
                itemService.updateItem(1L, 1L, itemDto1));
        assertEquals("Пользователь не может редактировать вещь", exception.getMessage());
    }

    @Test
    void getExistItemById() {
        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.findAllBookingItems(1L)).thenReturn(bookingList);
        when(commentRepository.findAllByItemId(1L)).thenReturn(commentList);
        when(bookingShortMapper.toBookingShortDto(booking1)).thenReturn(bookingShortDto1);
        when(bookingShortMapper.toBookingShortDto(booking2)).thenReturn(bookingShortDto2);
        when(commetMapper.toCommentListDto(commentList)).thenReturn(commentListDto);
        assertEquals(itemService.getById(1L, 1L), finalFull);
    }

    @Test
    void getNotExistItemById() {
        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemRepository.findById(5L)).thenReturn(Optional.empty());
        ItemNoFoundException exception = assertThrows(ItemNoFoundException.class, () -> itemService
                .getById(5L, 1L));
        assertEquals(String.format("Вещь по id %d не найдена", 5), exception.getMessage());
    }

    @Test
    void getAllItemsByUserTest() {
        when(itemRepository.findAllByOwnerIdOrderById(1L)).thenReturn(List.of(item1));

        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.findAllBookingItems(1L)).thenReturn(bookingList);
        when(commentRepository.findAllByItemId(1L)).thenReturn(commentList);
        when(bookingShortMapper.toBookingShortDto(booking1)).thenReturn(bookingShortDto1);
        when(bookingShortMapper.toBookingShortDto(booking2)).thenReturn(bookingShortDto2);
        when(commetMapper.toCommentListDto(commentList)).thenReturn(commentListDto);
        List<ItemFullDto> result = itemService.getAllItemsByUser(1L);
        assertEquals(List.of(finalFull), result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllItemsButItemNoExistByUserTest() {
        when(itemRepository.findAllByOwnerIdOrderById(1L)).thenThrow(new ItemNoFoundException("Вещей не найдено"));
        Exception e = assertThrows(ItemNoFoundException.class, () -> itemRepository
                .findAllByOwnerIdOrderById(1L));
        assertEquals("Вещей не найдено", e.getMessage());
    }

    @Test
    void search() {
        when(itemRepository.seachAll(anyString())).thenReturn(List.of(item1));
        when(itemMapper.toItemDtoList(List.of(item1))).thenReturn(List.of(itemDto1));
        assertEquals(itemService.search("текст"), List.of(itemDto1));
        assertEquals(itemService.search("текст").size(), 1);
    }

    @Test
    void addComment() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user1));
        when(userStorage.existsById(1L)).thenReturn(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.findAllBookingItems(1L)).thenReturn(bookingList);
        when(commentRepository.findAllByItemId(1L)).thenReturn(commentList);
        when(bookingShortMapper.toBookingShortDto(booking1)).thenReturn(bookingShortDto1);
        when(bookingShortMapper.toBookingShortDto(booking2)).thenReturn(bookingShortDto2);
        when(commetMapper.toCommentListDto(commentList)).thenReturn(commentListDto);
        when(itemFullMapper.itemFulltoModel(itemFullDto1)).thenReturn(item1);
        when(bookingRepository.findAllByItemAndBookerAndStatusAndEndBefore(any(), any(), any(), any()))
                .thenReturn(bookingList);
        when(commetMapper.toCommentModel(commentDto1)).thenReturn(comment1);
        when(commentRepository.save(comment1)).thenReturn(comment1);
        when(commetMapper.toCommentDto(comment1)).thenReturn(commentDto1);
        CommentDto commentDto = itemService.addComment(1L, commentDto1, 1L);
        assertEquals(commentDto, commentDto1);
    }

    @Test
    void addCommentUserNotRentedItem() {
        when(userStorage.findById(1L)).thenReturn(Optional.of(user1));
        when((userStorage.existsById(anyLong()))).thenReturn(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findAllByOwnerIdOrderById(1L)).thenThrow(new ItemNoFoundException("Вещей не найдено"));
        NoAvailableException exception = assertThrows(NoAvailableException.class, () -> itemService
                .addComment(1L, commentDto1, 1L));

        assertEquals(String.format("Пользователь не арендовал  вещь или аренда не закончена"), exception.getMessage());
    }
}