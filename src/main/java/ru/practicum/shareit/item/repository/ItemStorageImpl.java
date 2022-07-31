package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private static long id;
    private final Map<Long, Item> items = new ConcurrentHashMap<>();

    @Override
    public Item add(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(long id) {
        return items.values().stream()
                .filter(i -> i.getOwner().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(i -> i.getAvailable().equals(true)
                        && (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void validAvailable(ItemDto itemDto) throws ItemAvailableException {
        if (Objects.isNull(itemDto.getAvailable())) {
            throw new ItemAvailableException("Не может быть равным нулю");
        }
    }

    public void validOwner(long itemId, long userId) throws UserNotFoundException {
        if(!getItemById(itemId).getOwner().equals(userId)){
            throw new UserNotFoundException("Пользователь не является владельцем");
        }
    }
}
