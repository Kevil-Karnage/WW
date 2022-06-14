package com.program.WhoWins.service;

import com.program.WhoWins.entity.Event;
import com.program.WhoWins.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event addEvent(Event event) {
        return eventRepository.saveAndFlush(event);
    }

    @Override
    public Event getByName(String name) {
        return eventRepository.getByName(name);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event getById(long id) {
        return eventRepository.getById(id);
    }
}
