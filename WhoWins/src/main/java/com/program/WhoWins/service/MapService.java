package com.program.WhoWins.service;

import com.program.WhoWins.entity.Map;

import java.util.List;

public interface MapService {
    Map addMap(Map map);
    Map getByName(String name);
    List<Map> getAll();
}
