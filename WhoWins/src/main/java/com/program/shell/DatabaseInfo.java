package com.program.shell;

import com.program.WhoWins.entity.*;
import com.program.WhoWins.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DatabaseInfo {
    @Autowired
    public MatchService matchService = new MatchServiceImpl();
    @Autowired
    public MapService mapService = new MapServiceImpl();
    @Autowired
    public PlayedMapService playedMapService = new PlayedMapServiceImpl();
    @Autowired
    public TeamService teamService = new TeamServiceImpl();
    @Autowired
    public EventService eventService = new EventServiceImpl();
    @Autowired
    public PredictionService predictionService = new PredictionServiceImpl();

    public DatabaseInfo() {
    }

    public DatabaseInfo(MatchService matchService,
                        MapService mapService,
                        PlayedMapService playedMapService,
                        TeamService teamService,
                        EventService eventService,
                        PredictionService predictionService) {
        this.matchService = matchService;
        this.mapService = mapService;
        this.playedMapService = playedMapService;
        this.teamService = teamService;
        this.eventService = eventService;
        this.predictionService = predictionService;
    }

    public Match saveMatch(MatchInfo matchInfo, boolean isEndedMatch) {
        // переводи MatchInfo в Match
        Match match = MatchInfo.convertMatchInfoToMatch(
                matchInfo,
                saveEvent(matchInfo.event),
                saveTeam(matchInfo.team1),
                saveTeam(matchInfo.team2));

        // ищем матч в бд
        Match fromDB = matchService.getByEventTeamsAndDate(
                match.getEventId(), match.getIdTeam1(), match.getIdTeam2(), match.getDate());

        // если матч уже был добавлен до этого как будущий матч
        if (match.equals(fromDB)) {
            // то обновляем инфу о нём
            match = fromDB;
            // если сохраняем законченный матч и в бд он не сохранён
            if (isEndedMatch && !match.isEnded()) {
                // то обновляем информацию
                match.setEnded(true);
                // сохраняем карты
                saveEndedMaps(match, matchInfo);
                // проверяем наличие прогнозов
                checkPredictionsOnMatch(match.getId());
                // и сохраняем матч
                return matchService.addMatch(match);
            }
            // иначе не сохраняем
            return null;
        } else {
            // сохраняем матч в бд
            return matchService.addMatch(match);
        }
    }

    private void saveEndedMaps(Match match, MatchInfo matchInfo) {
        // сохраняем сыгранные в матче карты
        for (int i = 0; i < matchInfo.maps.length; i++) {
            if (matchInfo.maps[i] != null)
                saveMap(matchInfo.maps[i], match.getId());
        }
    }

    private void checkPredictionsOnMatch(long matchId) {
        // проверяем наличие прогноза на этот матч
        Prediction prediction = predictionService.getByMatchId(matchId);
        // если он существует и актуален (матч ещё не закончен)
        if (prediction != null && !prediction.isEnded()) {
            // получаем id пбедителя
            long winnerTeamId = getWinnerTeamId(matchId);
            // обновляем информацию
            prediction.setEnded(true);
            prediction.setCorrect(winnerTeamId == prediction.getWinnerId());
            // и сохраняем в бд
            predictionService.addPrediction(prediction);
        }
    }

    public long saveEvent(String eventName) {
        Event event = eventService.getByName(eventName);
        if (event == null) {
            event = new Event();
            event.setName(eventName);
            eventService.addEvent(event);
        }
        return event.getId();
    }

    public long saveTeam(String teamName) {
        Team team = teamService.getByName(teamName);
        if (team == null) {
            team = new Team();
            team.setName(teamName);
            teamService.addTeam(team);
        }
        return team.getId();
    }

    public void saveMap(MapInfo mapInfo, long matchId) {
        Map map = mapService.getByName(mapInfo.map);
        if (map == null) {
            map = new Map();
            map.setName(mapInfo.map);
            mapService.addMap(map);
        }
        PlayedMap playedMap = new PlayedMap();
        playedMap.setMapId(map.getId());
        playedMap.setMatchId(matchId);
        playedMap.setScoreTeam1(mapInfo.score1);
        playedMap.setScoreTeam2(mapInfo.score2);
        playedMapService.addPlayedMap(playedMap);
    }

    public Prediction savePrediction(PredictionInfo predictionInfo) {
        Prediction prediction = new Prediction();

        prediction.setMatchId(predictionInfo.getMatchId());
        prediction.setPoints1(predictionInfo.getPoints1());
        prediction.setPoints2(predictionInfo.getPoints2());
        long teamId;
        if (predictionInfo.isFirstWin()) {
            teamId = teamService.getByName(predictionInfo.getTeam1()).getId();
        } else {
            teamId = teamService.getByName(predictionInfo.getTeam2()).getId();
        }
        prediction.setWinnerId(teamId);

        if (predictionService.getByMatchId(predictionInfo.matchId) != null) {
            return null;
        }

        //if (predictionInfo.canSave()) {
            prediction = predictionService.addPrediction(prediction);
            return prediction;
        //}

        //return null;
    }

    private long getWinnerTeamId(long matchId) {
        Match match = matchService.getById(matchId);
        if (!match.isEnded()) return -1;

        int count = 0;
        List<PlayedMap> playedMaps = playedMapService.getByMatchId(matchId);
        for (PlayedMap pm : playedMaps) {
            if (pm.getScoreTeam1() > pm.getScoreTeam2()) {
                count++;
            } else {
                count--;
            }
        }

        return count > 0 ? match.getIdTeam1() : match.getIdTeam2();
    }
}

class DatabaseInfoException extends Exception {
    public DatabaseInfoException(String message){
        super(message);
    }
}
