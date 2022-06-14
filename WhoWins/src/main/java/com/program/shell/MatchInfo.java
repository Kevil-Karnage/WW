package com.program.shell;

import com.program.WhoWins.entity.Match;
import com.program.WhoWins.entity.PlayedMap;

import java.util.Date;
import java.util.List;

public class MatchInfo {
    public String team1;
    public String team2;
    public String event;
    public int stars;
    public int matchType;
    public Date date;
    public MapInfo[] maps;
    public int positionHLTV1;
    public int positionHLTV2;
    public boolean ended;

    public static Match convertMatchInfoToMatch(MatchInfo matchInfo, long eventId, long team1Id, long team2Id) {
        Match match = new Match();

        match.setEventId(eventId);
        match.setIdTeam1(team1Id);
        match.setIdTeam2(team2Id);
        match.setDate(matchInfo.date);
        match.setStars(matchInfo.stars);
        match.setType(matchInfo.matchType);
        match.setEnded(matchInfo.ended);
        match.setPositionHLTV1(matchInfo.positionHLTV1);
        match.setPositionHLTV2(matchInfo.positionHLTV2);

        return match;
    }

    public static MatchInfo convertMatchToMatchInfo(DatabaseInfo dbInfo, Match match) {
        MatchInfo mi = new MatchInfo();
        mi.matchType = match.getType();
        mi.ended = match.isEnded();
        mi.positionHLTV1 = match.getPositionHLTV1();
        mi.positionHLTV2 = match.getPositionHLTV2();
        mi.date = match.getDate();
        mi.stars = match.getStars();

        mi.event = dbInfo.eventService.getById(match.getEventId()).getName();
        mi.team1 = dbInfo.teamService.getById(match.getIdTeam1()).getName();
        mi.team2 = dbInfo.teamService.getById(match.getIdTeam2()).getName();

        mi.maps = new MapInfo[mi.matchType];
        List<PlayedMap> maps = dbInfo.playedMapService.getByMatchId(match.getId());
        for (int i = 0; i < maps.size(); i++) {
            mi.maps[i] = convertPlayedMapsToMapsInfo(dbInfo, maps.get(i));
        }

        return mi;
    }

    public static MapInfo convertPlayedMapsToMapsInfo(DatabaseInfo dbInfo, PlayedMap playedMap) {
        MapInfo mapInfo = new MapInfo();
        mapInfo.map = dbInfo.teamService.getById(playedMap.getId()).getName();
        mapInfo.score1 = playedMap.getScoreTeam1();
        mapInfo.score2 = playedMap.getScoreTeam2();

        return mapInfo;
    }
}
