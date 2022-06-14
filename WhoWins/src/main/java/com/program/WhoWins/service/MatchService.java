package com.program.WhoWins.service;

import com.program.WhoWins.entity.Match;

import java.util.Date;
import java.util.List;

public interface MatchService {

    Match addMatch(Match match);
    Match getById(long id);
    List<Match> getAll();
    List<Match> getByTeamId(long teamId);
    List<Match> getByTeamIdOrderByDate(long teamId);
    List<Match> getByEventId(long eventId);
    Match getByEventTeamsAndDate(long eventId, long idTeam1, long idTeam2, Date date);
    void setEnded(long matchId, boolean ended);
    List<Match> getMatchesByEndedValue(boolean ended);
}
