package com.program.prediction;

import com.program.WhoWins.entity.Match;
import com.program.WhoWins.entity.PlayedMap;
import com.program.WhoWins.entity.Team;
import com.program.shell.DatabaseInfo;
import com.program.shell.PredictionInfo;

import java.util.Date;
import java.util.List;

public class PredictionGenerator {

    long week = 604800000;

    DatabaseInfo dbInfo;

    public PredictionGenerator(DatabaseInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public PredictionInfo create(String team1, String team2, Date dateOfMatch) {
        PredictionInfo prediction = new PredictionInfo();
        prediction.setTeam1(team1);
        prediction.setTeam2(team2);
        if (dbInfo.teamService.getByName(team1) == null ||
                dbInfo.teamService.getByName(team2) == null) {
            return null;
        }
        return createPredict(prediction, dateOfMatch);
    }

    public PredictionInfo create(Match m) {
        PredictionInfo predictionInfo = new PredictionInfo();
        predictionInfo.setMatchId(m.getId());
        predictionInfo.setTeam1(dbInfo.teamService.getById(m.getIdTeam1()).getName());
        predictionInfo.setTeam2(dbInfo.teamService.getById(m.getIdTeam2()).getName());
        return createPredict(predictionInfo, m.getDate());
    }

    private PredictionInfo createPredict(PredictionInfo pi, Date dateOfMatch) {
        double points1;
        double points2;
        try {
            points1 = getPointsOfTeam(pi.getTeam1(), dateOfMatch);
            pi.setPoints1(points1);
            points2 = getPointsOfTeam(pi.getTeam2(), dateOfMatch);
            pi.setPoints2(points2);
        } catch (PredictionException e) {
            System.out.println(e);
            //pi.setResultInfo(e.getMessage());
            return pi;
        }
        pi.setFirstWin(points1 > points2);
//        pi.setResultInfo("Прогноз успешно сделан");
        return pi;
    }

    private double getPointsOfTeam(String teamName, Date dateOfMatch) throws PredictionException {
        Team team = dbInfo.teamService.getByName(teamName);
        List<Match> matches = dbInfo.matchService.getByTeamIdOrderByDate(team.getId());

        if (matches.size() < 3) {
            throw new PredictionException("Слишком мало матчей");
        }

        double allPoints = 0;
        double countMaps = 0;
        for (Match m : matches) {
            long timeDifference = dateOfMatch.getTime() - m.getDate().getTime();
            if (timeDifference > (week * 3)) {
                //throw new PredictionException("Прошло слишком много времени с последнего матча");
            }
            double koef = getKoef(timeDifference);

            double matchPoints = 0;

            List<PlayedMap> playedMaps = dbInfo.playedMapService.getByMatchId(m.getId());
            for (PlayedMap pm: playedMaps) {
                double mapPoints = m.getIdTeam1().equals(team.getId()) ?
                        16 + pm.getScoreTeam1() - pm.getScoreTeam2() :
                        16 + pm.getScoreTeam2() - pm.getScoreTeam1();

                matchPoints += (mapPoints * 100 * koef) * (m.getStars() + 1);
            }
            countMaps += playedMaps.size();
            allPoints += matchPoints;
        }
        return allPoints / countMaps;
    }

    private double getKoef(long time) {
        long countWeeks = time / week;
        double koef = Math.pow(0.95, (int) countWeeks);
        return koef < 0 ? 0 : koef;
    }
}

class PredictionException extends Exception {
    public PredictionException(String message){
        super(message);
    }
}
