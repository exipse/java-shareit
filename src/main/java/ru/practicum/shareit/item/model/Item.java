package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.model.Comment;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "ITEMS", schema = "public")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "request_id")
    private int request;

    @Transient
    private List<Booking> booking;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;
}
