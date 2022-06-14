package com.program.WhoWins.service;

import com.program.WhoWins.entity.PlayedMap;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayedMapService {
    PlayedMap addPlayedMap(PlayedMap map);
    List<PlayedMap> getAll();
    List<PlayedMap> getByMatchId(long matchId);
    List<PlayedMap> getByMapId(long mapId);
    PlayedMap getByMapAndMatchId(long mapId, long matchId);
}
