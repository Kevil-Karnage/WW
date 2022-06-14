package com.program.WhoWins.service;

import com.program.WhoWins.entity.Team;
import com.program.WhoWins.repository.TeamRepository;
import com.program.WhoWins.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team addTeam(Team team) {
        return teamRepository.saveAndFlush(team);
    }

    @Override
    public Team getById(long id) {
        return teamRepository.getById(id);
    }

    @Override
    public Team getByName(String name) {
        return teamRepository.getByName(name);
    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }
}
