package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MapRepository extends JpaRepository<Map, Long> {
    @Query("select b from Map b where b.name = :name")
    Map getByName(String name);
}
