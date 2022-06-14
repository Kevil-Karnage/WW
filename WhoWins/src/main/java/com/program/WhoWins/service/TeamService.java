package com.program.WhoWins.service;

import com.program.WhoWins.entity.Team;

import java.util.List;

public interface TeamService {
    Team addTeam(Team team);
    Team getById(long id);
    Team getByName(String name);
    List<Team> getAll();


}
