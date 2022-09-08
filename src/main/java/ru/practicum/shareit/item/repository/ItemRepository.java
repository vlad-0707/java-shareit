package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
            " and i.available = true")
    Page<Item> search(String text, Pageable pageable);

    Page<Item> findAllByOwnerIdOrderByIdAsc(Long userId, Pageable pageable);

    Item findByIdOrderByIdDesc(long itemId) throws UserNotFoundException;

    List<Item> findByRequestId(Long requestId);

}
