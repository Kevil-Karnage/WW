package com.program.WhoWins.service;

import com.program.WhoWins.entity.Map;

import java.util.List;

public interface MapService {
    Map addPlayedMap(Map map);
    List<Map> getAll();
    List<Map> getByMatchId(long matchId);
    List<Map> getByMapId(long mapId);
    Map getByMapAndMatchId(long mapId, long matchId);
}
