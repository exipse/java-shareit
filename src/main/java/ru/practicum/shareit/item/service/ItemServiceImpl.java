package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingShortMapper bookingShortMapper;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;
    private final ItemFullMapper itemFullMapper;
    private final CommetMapper commetMapper;

    @Override
    public ItemDto createItem(ItemDto item, Long userId) {

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new UserNoFoundException("Пользователя не существует"));
        item.setOwnerId(user.getId());
        Item storageItem = itemRepository.save(itemMapper.toItemModel(item));
        log.info("Вещь сохранена");
        return itemMapper.toItemDto(storageItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto item) {

        Item itemFromDB = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNoFoundException(String.format("Вещь по id %s не найдена", itemId)));
        if (!Objects.equals(itemFromDB.getOwnerId(), userId)) {
            throw new UserNoAccessException(String.format("Пользователь с id %d не может редактировать вещь с id %d",
                    userId, itemId));
        }
        if (item.getName() != null) {
            itemFromDB.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromDB.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromDB.setAvailable(item.getAvailable());
        }
        Item storageItem = itemRepository.save(itemFromDB);
        log.info(String.format("Вещь по id = %s обновлена", itemId));

        return itemMapper.toItemDto(storageItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemFullDto getById(Long itemId, Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new UserNoFoundException("Пользователя не существует");
        }
        Optional<Item> itemFromRepository = itemRepository.findById(itemId);
        if (!itemFromRepository.isPresent()) {
            throw new ItemNoFoundException(String.format("Вещь по id %s не найдена", itemId));

        }
        Item item = itemFromRepository.get();
        ItemFullDto itemdto = itemFullMapper.itemFulltoDto(item);

        if (userId.equals(item.getOwnerId())) {
            LocalDateTime time = LocalDateTime.now();
            List<Booking> userBookings = bookingRepository.findAllBookingItems(itemId);

            Booking lastBooking = userBookings.stream().filter(book -> !(book.getStatus().equals(Status.REJECTED)))
                    .filter(book -> book.getStart().isBefore(time))
                    .max((book1, book2) -> book1.getEnd().compareTo(book2.getEnd())).orElse(null);

            Booking nextBooking = userBookings.stream().filter(book -> !(book.getStatus().equals(Status.REJECTED)))
                    .filter(book -> book.getStart().isAfter(time))
                    .min((book1, book2) -> book1.getEnd().compareTo(book2.getEnd())).orElse(null);

            List<Comment> comments = commentRepository.findAllByItemId(itemId);
            itemdto.setLastBooking(bookingShortMapper.toBookingShortDto(lastBooking));
            itemdto.setNextBooking(bookingShortMapper.toBookingShortDto(nextBooking));
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        itemdto.setComments(commetMapper.toCommentListDto(comments));
        log.info(String.format("Вещь по id = %s получена", itemId));

        return itemdto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFullDto> getAllItemsByUser(Long userId) {
        List<ItemFullDto> userItems = itemRepository.findAllByOwnerIdOrderById(userId)
                .stream().map(itemFullMapper::itemFulltoDto).collect(Collectors.toList());
        List<ItemFullDto> fullusers = userItems.stream().map(obj -> getById(obj.getId(), obj.getOwnerId()))
                .collect(Collectors.toList());
        log.info(String.format("Вещь пользователя id = %s получены", userId));

        return fullusers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        List<Item> items = itemRepository.seachAll(text);
        log.info(String.format("Количество найденных вещей = %d", items.size()));
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public CommentDto addComment(Long userId, CommentDto commentDto, Long itemId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new UserNoFoundException("Пользователя не существует"));

        Item item = itemFullMapper.itemFulltoModel(getById(itemId, userId));
        LocalDateTime date = LocalDateTime.now();

        List<Booking> bookings = bookingRepository
                .findAllByItemAndBookerAndStatusAndEndBefore(item, user, Status.APPROVED, LocalDateTime.now());

        if (!bookings.isEmpty()) {
            Comment comment = commetMapper.toCommentModel(commentDto);
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(date);
            return commetMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new NoAvailableException("Пользователь не арендовал  вещь или аренда не закончена");
        }
    }

}
