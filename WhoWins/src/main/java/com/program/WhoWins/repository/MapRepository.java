package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, Long> {
    @Query("select b from Map b where b.matchId = :matchId")
    List<Map> getByMatchId(long matchId);
    @Query("select b from Map b where b.mapTypeId = :mapTypeId")
    List<Map> getByMapId(long mapTypeId);
    @Query("select b from Map b where b.mapTypeId = :mapTypeId and b.matchId = :matchId")
    Map getByMapAndMatchId(long mapTypeId, long matchId);
}
