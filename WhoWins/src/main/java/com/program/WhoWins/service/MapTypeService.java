package com.program.WhoWins.service;

import com.program.WhoWins.entity.MapType;

import java.util.List;

public interface MapTypeService {
    MapType addMapType(MapType mapType);
    MapType getByName(String name);
    MapType getById(long id);
    List<MapType> getAll();

}
