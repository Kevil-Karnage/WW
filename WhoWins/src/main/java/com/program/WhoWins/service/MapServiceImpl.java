package com.program.WhoWins.service;

import com.program.WhoWins.entity.Map;
import com.program.WhoWins.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    MapRepository mapRepository;

    public Map addMap(Map map) {
        return mapRepository.saveAndFlush(map);
    }

    public Map getByName(String name) {
        return mapRepository.getByName(name);
    }

    public List<Map> getAll() { return mapRepository.findAll();
    }
}
