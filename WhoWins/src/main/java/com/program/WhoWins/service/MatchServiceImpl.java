package com.program.WhoWins.service;

import com.program.WhoWins.entity.Match;
import com.program.WhoWins.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Match addMatch(Match match) {
        return matchRepository.saveAndFlush(match);
    }

    @Override
    public Match getById(long id) {
        return matchRepository.getById(id);
    }

    @Override
    public List<Match> getByTeamId(long teamId) {
        return matchRepository.getByTeamId(teamId);
    }

    @Override
    public List<Match> getByTeamIdOrderByDate(long teamId) {
        return matchRepository.getByTeamIdOrderByDate(teamId);
    }

    @Override
    public List<Match> getByEventId(long eventId) {
        return matchRepository.getByEventId(eventId);
    }

    @Override
    public Match getByEventTeamsAndDate(long eventId, long idTeam1, long idTeam2, Date date) {
        return matchRepository.getByEventTeamsAndDate(eventId, idTeam1, idTeam2, date);
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public void setEnded(long matchId, boolean ended) {
        matchRepository.setEnded(matchId, ended);
    }

    @Override
    public List<Match> getMatchesByEndedValue(boolean ended) {
        return matchRepository.getMatchesByEndedValue(ended);
    }
}
