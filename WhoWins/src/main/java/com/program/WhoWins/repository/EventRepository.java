package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select b from Event b where b.name = :name")
    Event getByName(String name);
}
