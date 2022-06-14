package com.program.WhoWins.service;

import com.program.WhoWins.entity.Event;

import java.util.List;

public interface EventService {

    Event addEvent(Event event);
    Event getByName(String name);
    List<Event> getAll();
    Event getById(long id);
}
