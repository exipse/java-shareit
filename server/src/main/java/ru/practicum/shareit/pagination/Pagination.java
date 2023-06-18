package ru.practicum.shareit.pagination;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class Pagination extends PageRequest {

    protected Pagination(int from, int size) {
        super(from, size, Sort.unsorted());
    }

    public static Pageable getInfo(int from, int size) {
        return new Pagination(from / size, size);
    }

}