package com.program.WhoWins.service;

import com.program.WhoWins.entity.MapType;
import com.program.WhoWins.repository.MapTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapTypeServiceImpl implements MapTypeService {

    @Autowired
    MapTypeRepository mapTypeRepository;

    public MapType addMapType(MapType mapType) {
        return mapTypeRepository.saveAndFlush(mapType);
    }

    public MapType getByName(String name) {
        return mapTypeRepository.getByName(name);
    }

    @Override
    public MapType getById(long id) {
        return mapTypeRepository.getById(id);
    }

    public List<MapType> getAll() { return mapTypeRepository.findAll();
    }
}
