package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.RequestNoFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortForRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemWithAnswersRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapping;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private UserStorage userStorage;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemRequestStorage requestStorage;
    @Mock
    private ItemRepository itemStorage;
    @Mock
    private RequestMapping requestMapping;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    ItemRequestServiceImpl requestService;

    private Item item;
    private ItemDto itemDto;

    private List<Item> itemList;

    private User user;
    private UserDto userDto;

    private ItemRequest request;
    private ItemRequestDto requestDto;
    private List<ItemRequest> requestList;

    private ItemWithAnswersRequestDto withAnswersRequestDto;
    private ItemShortForRequestDto itemShortForRequestDto;
    private List<ItemShortForRequestDto> itemShortForRequestDtos;
    private List<ItemWithAnswersRequestDto> requesWithAnswerstDtos;


    @BeforeEach
    void before() {
        item = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
        itemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, 1L, 1L);

        itemList = List.of(item);

        user = new User(1L, "user1", "user1@user.com");
        userDto = new UserDto(1L, "user1", "user1@user.com");

        request = new ItemRequest(1L, "Запрос", user, LocalDateTime.now());
        requestDto = new ItemRequestDto(1L, "Запрос", LocalDateTime.now(), userDto);

        requestList = List.of(request);

        itemShortForRequestDto = new ItemShortForRequestDto(1L, "Дрель", 1L,
                "Простая дрель", true, 1L);
        itemShortForRequestDtos = List.of(itemShortForRequestDto);
        withAnswersRequestDto = new ItemWithAnswersRequestDto(1L, "Простая дрель",
                LocalDateTime.now(), userDto, itemShortForRequestDtos);

        requesWithAnswerstDtos = List.of(withAnswersRequestDto);

    }

    @Test
    void addNewRequest() {

        when(userStorage.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toUserModel(any())).thenReturn(user);

        when(requestMapping.toRequestModel(requestDto)).thenReturn(request);
        when(requestStorage.save(request)).thenReturn(request);
        when(requestMapping.toRequestDto(request)).thenReturn(requestDto);

        assertEquals(requestService.addNewRequest(requestDto, 1L), requestDto);
    }

    @Test
    void getOwnRequests() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(requestStorage.findAllByRequestor_IdOrderByCreatedDesc(anyLong())).thenReturn(requestList);
        when(requestMapping.toRequestWithAnswerListDto(requestList)).thenReturn(requesWithAnswerstDtos);

        when(itemStorage.findAllByRequestId(anyLong())).thenReturn(itemList);
        when(itemMapper.toItemShortForReqDtos(itemList)).thenReturn(itemShortForRequestDtos);

        assertEquals(requestService.getOwnRequests(1L), requesWithAnswerstDtos);
    }

    @Test
    void getUserRequests() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(userService.get(anyLong())).thenReturn(userDto);
        when(requestStorage.findAllByRequestor_IdIsNotOrderByCreated(anyLong(), any())).thenReturn(requestList);
        when(requestMapping.toRequestWithAnswerListDto(requestList)).thenReturn(requesWithAnswerstDtos);

        when(itemStorage.findAllByRequestId(anyLong())).thenReturn(itemList);
        when(itemMapper.toItemShortForReqDtos(itemList)).thenReturn(itemShortForRequestDtos);

        assertEquals(requestService.getUserRequests(1, 1, 1L), requesWithAnswerstDtos);
    }

    @Test
    void getRequestById() {
        when(requestStorage.findById(anyLong())).thenReturn(Optional.of(request));
        when(userStorage.existsById(anyLong())).thenReturn(true);

        when(requestMapping.toRequestWithAnswerDto(request)).thenReturn(withAnswersRequestDto);
        assertEquals(requestService.getRequestById(1L, 1L), withAnswersRequestDto);
    }

    @Test
    void getRequesButIdtNotFound() {

        when(requestStorage.findById(anyLong())).thenReturn(Optional.empty());

        RequestNoFoundException exception = assertThrows(RequestNoFoundException.class,
                () -> requestService.getRequestById(1L, 1L));

        assertEquals(String.format("Запроса по id = 1 не существует"), exception.getMessage());
    }
}