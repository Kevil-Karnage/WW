package com.program.shell;

import com.program.WhoWins.entity.Event;
import com.program.WhoWins.entity.Match;
import com.program.WhoWins.entity.Map;

import java.util.Date;
import java.util.List;

public class MatchInfo {
    public long hltvId;
    public String team1;
    public String team2;
    public EventInfo event;
    public int stars;
    public int matchType;
    public Date date;
    public MapInfo[] maps;
    public int positionHLTV1;
    public int positionHLTV2;
    public boolean ended;

    public static Match convertMatchInfoToMatch(MatchInfo matchInfo, long eventId, long team1Id, long team2Id) {
        Match match = new Match();

        match.setHltvId(matchInfo.hltvId);
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
        mi.hltvId = match.getHltvId();
        mi.matchType = match.getType();
        mi.ended = match.isEnded();
        mi.positionHLTV1 = match.getPositionHLTV1();
        mi.positionHLTV2 = match.getPositionHLTV2();
        mi.date = match.getDate();
        mi.stars = match.getStars();

        mi.team1 = dbInfo.teamService.getById(match.getIdTeam1()).getName();
        mi.team2 = dbInfo.teamService.getById(match.getIdTeam2()).getName();


        mi.event = convertEventToEventInfo(dbInfo, match.getEventId());

        mi.maps = new MapInfo[mi.matchType];
        List<Map> maps = dbInfo.mapService.getByMatchId(match.getId());
        for (int i = 0; i < maps.size(); i++) {
            mi.maps[i] = convertPlayedMapsToMapsInfo(dbInfo, maps.get(i));
        }

        return mi;
    }

    public static EventInfo convertEventToEventInfo(DatabaseInfo dbInfo, Long eventId) {
        Event event = dbInfo.eventService.getById(eventId);

        EventInfo eventInfo = new EventInfo();
        eventInfo.name = event.getName();
        eventInfo.beginDate = event.getBeginDate();
        eventInfo.endDate = event.getEndDate();
        return eventInfo;
    }

    public static MapInfo convertPlayedMapsToMapsInfo(DatabaseInfo dbInfo, Map map) {
        MapInfo mapInfo = new MapInfo();
        mapInfo.map = dbInfo.teamService.getById(map.getId()).getName();
        mapInfo.score1 = map.getScoreTeam1();
        mapInfo.score2 = map.getScoreTeam2();

        return mapInfo;
    }
}
