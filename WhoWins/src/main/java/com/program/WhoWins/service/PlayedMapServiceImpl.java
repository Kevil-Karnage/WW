package com.program.WhoWins.service;

import com.program.WhoWins.entity.PlayedMap;
import com.program.WhoWins.repository.PlayedMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayedMapServiceImpl implements PlayedMapService {

    @Autowired
    private PlayedMapRepository playedMapRepository;

/*
    public PlayedMapServiceImpl(PlayedMapRepository playedMapRepository) {
        this.playedMapRepository = playedMapRepository;
    }
*/

    @Override
    public PlayedMap addPlayedMap(PlayedMap map) {
        return playedMapRepository.saveAndFlush(map);
    }

    @Override
    public List<PlayedMap> getAll() {
        return playedMapRepository.findAll();
    }

    @Override
    public List<PlayedMap> getByMatchId(long matchId) {
        return playedMapRepository.getByMatchId(matchId);
    }

    @Override
    public List<PlayedMap> getByMapId(long mapId) {
        return playedMapRepository.getByMapId(mapId);
    }

    @Override
    public PlayedMap getByMapAndMatchId(long mapId, long matchId) {
        return playedMapRepository.getByMapAndMatchId(mapId, matchId);
    }
}
