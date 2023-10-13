package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public static Comment fromCommentDto(CommentDto commentDto, Item item, User user) {
        return new Comment(
                commentDto.getId(),
            commentDto.getText(),
            item,
            user
        );
    }

    public static CommentFullDto toCommentFullDto(Comment comment, LocalDateTime time) {
        return new CommentFullDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                time
        );
    }
}
