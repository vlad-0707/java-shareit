package ru.practicum.shareit;

import org.springframework.stereotype.Component;

@Component
public class Identifier {
    private long id;

    public long getId(){
        return ++id;
    }
}
