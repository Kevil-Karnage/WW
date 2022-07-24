package com.program.WhoWins.service;

import com.program.WhoWins.entity.Map;
import com.program.WhoWins.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private MapRepository mapRepository;


    @Override
    public Map addPlayedMap(Map map) {
        return mapRepository.saveAndFlush(map);
    }

    @Override
    public List<Map> getAll() {
        return mapRepository.findAll();
    }

    @Override
    public List<Map> getByMatchId(long matchId) {
        return mapRepository.getByMatchId(matchId);
    }

    @Override
    public List<Map> getByMapId(long mapId) {
        return mapRepository.getByMapId(mapId);
    }

    @Override
    public Map getByMapAndMatchId(long mapId, long matchId) {
        return mapRepository.getByMapAndMatchId(mapId, matchId);
    }
}
