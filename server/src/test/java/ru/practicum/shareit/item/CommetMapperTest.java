package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommetMapper;
import ru.practicum.shareit.item.comment.mapper.CommetMapperImpl;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommetMapperImpl.class, ItemMapperImpl.class})
class CommetMapperTest {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final ItemDto itemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final CommentDto commentDto = new CommentDto(1L, "коммент", itemDto, "user1", localDateTime);
    private final Item item = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final User user = new User(1L, "user1", "user1@user.com");
    private final Comment comment = new Comment(1L, "коммент", item, user, localDateTime);
    private final CommentDto commentDto1 = new CommentDto();
    private final Item item1 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final User user1 = new User(1L, null, "user1@user.com");
    private final Comment comment1 = new Comment(1L, "коммент", item1, user1, localDateTime);
    private final Item item2 = new Item(1L, "Дрель", "Простая дрель", true, 1L, 1L);
    private final Comment comment2 = new Comment(1L, "коммент", item2, null, localDateTime);
    @Autowired
    private CommetMapper mapper;

    @Test
    public void toCommentDtoTest() {
        CommentDto commentDto = mapper.toCommentDto(comment);
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
    }

    @Test
    public void toCommentListDtoTest() {
        List<CommentDto> commentDtos = mapper.toCommentListDto(List.of(comment));
        commentDto1.setText("Дрель2");
        assertEquals(comment.getText(), commentDtos.get(0).getText());
        assertEquals(comment.getAuthor().getName(), commentDtos.get(0).getAuthorName());
        assertEquals(1, commentDtos.size());
    }

    @Test
    public void toCommentListDtoTestNull() {
        List<Comment> comments = null;
        List<CommentDto> commentDtos = mapper.toCommentListDto(comments);
        assertNull(commentDtos);
    }

    @Test
    public void toCommentListDto() {
        List<CommentDto> commentDtos = mapper.toCommentListDto(List.of(comment));
        assertEquals(comment.getText(), commentDtos.get(0).getText());
        assertEquals(comment.getId(), commentDtos.get(0).getId());
        assertEquals(1, commentDtos.size());
    }

    @Test
    public void toCommentListDtoNull() {
        List<Comment> comments = null;
        List<CommentDto> commentDtos = mapper.toCommentListDto(comments);

        assertNull(commentDtos);
    }

    @Test
    public void toCommentDtoTestNoAuthor() {
        CommentDto commentDto = mapper.toCommentDto(comment2);
        assertEquals(comment2.getText(), commentDto.getText());
        assertNull(comment2.getAuthor());
    }

    @Test
    public void toCommentDtoTestNoAuthorName() {
        CommentDto commentDto = mapper.toCommentDto(comment1);
        assertEquals(comment1.getText(), commentDto.getText());
        assertNull(comment1.getAuthor().getName());
    }

    @Test
    public void toCommentDtoTestNull() {
        CommentDto commentDto = mapper.toCommentDto(null);
        assertNull(commentDto);
    }

    @Test
    public void toCommentModelTest() {
        Comment comment = mapper.toCommentModel(commentDto);
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getAuthorName(), comment.getAuthor().getName());
    }

    @Test
    public void toCommentModelTestNull() {
        Comment comment = mapper.toCommentModel(null);
        assertNull(comment);
    }
}