package com.program.WhoWins.repository;

import com.program.WhoWins.entity.PlayedMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayedMapRepository extends JpaRepository<PlayedMap, Long> {
    @Query("select b from PlayedMap b where b.matchId = :matchId")
    List<PlayedMap> getByMatchId(long matchId);
    @Query("select b from PlayedMap b where b.mapId = :mapId")
    List<PlayedMap> getByMapId(long mapId);
    @Query("select b from PlayedMap b where b.mapId = :mapId and b.matchId = :matchId")
    PlayedMap getByMapAndMatchId(long mapId, long matchId);
}
