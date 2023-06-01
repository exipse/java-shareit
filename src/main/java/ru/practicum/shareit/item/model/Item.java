package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ITEMS", schema = "public")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "request_id")
    private Long requestId;
}
