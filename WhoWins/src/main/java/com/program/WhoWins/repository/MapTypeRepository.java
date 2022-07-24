package com.program.WhoWins.repository;

import com.program.WhoWins.entity.MapType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MapTypeRepository extends JpaRepository<MapType, Long> {
    @Query("select b from MapType b where b.name = :name")
    MapType getByName(String name);
}
