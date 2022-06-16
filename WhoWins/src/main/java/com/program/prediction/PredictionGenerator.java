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

    PredictionInfo lastPredict;

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
        lastPredict = createPredict(prediction, dateOfMatch);
        return lastPredict;
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
        lastPredict = pi;
        try {
            points1 = getPointsOfTeam(pi.getTeam1(), dateOfMatch);
            pi.setPoints1(points1);
            points2 = getPointsOfTeam(pi.getTeam2(), dateOfMatch);
            pi.setPoints2(points2);
        } catch (PredictionException e) {
            System.out.println(e);
            return pi;
        }
        pi.setFirstWin(points1 > points2);
        return pi;
    }

    private double getPointsOfTeam(String teamName, Date dateOfMatch) throws PredictionException {
        Team team = dbInfo.teamService.getByName(teamName);
        List<Match> matches = dbInfo.matchService.getByTeamIdOrderByDate(team.getId());

        if (matches.size() < 3) {
            if (matches.size() == 0)
                throw new PredictionException("Слишком мало матчей");
            lastPredict.incrementPredictPoints();
        }

/*        if (dateOfMatch.getTime() - matches.get(0).getDate().getTime() > week * 3) {
            lastPredict.incrementPredictPoints();
        }
*/
        double allPoints = 0;
        double countMaps = 0;

        int countMatches = 0;
        for (Match m : matches) {
            long timeDifference = dateOfMatch.getTime() - m.getDate().getTime();
            if (timeDifference < (week * 3)) {
                countMatches++;
            }

            double timeKoef = getTimeKoef(timeDifference);
            double ratingKoef = getRatingKoef(m.getPositionHLTV1(), m.getPositionHLTV2());

            double matchPoints = 0;

            List<PlayedMap> playedMaps = dbInfo.playedMapService.getByMatchId(m.getId());
            for (PlayedMap pm: playedMaps) {
                double mapPoints = m.getIdTeam1().equals(team.getId()) ?
                        16 + pm.getScoreTeam1() - pm.getScoreTeam2() :
                        16 + pm.getScoreTeam2() - pm.getScoreTeam1();


                mapPoints = mapPoints * timeKoef;
                //mapPoints = mapPoints * ratingKoef;
                mapPoints = mapPoints * (m.getStars() + 1);
                matchPoints += mapPoints;
            }
            countMaps += playedMaps.size();
            allPoints += matchPoints;
        }

        if (countMatches < 3) {
            lastPredict.incrementPredictPoints();
        }

        return allPoints / countMaps;
    }

    private double getRatingKoef(int hltvPos1, int hltvPos2) {
        double koef;

        MatchClass firstClass = MatchClass.indexOf(hltvPos1);
        MatchClass secondClass = MatchClass.indexOf(hltvPos2);

        koef = firstClass.rank + secondClass.rank;

        return koef;
    }

    private double getTimeKoef(long time) {
        long countWeeks = time / week;
        double koef = Math.pow(0.95, (int) countWeeks);
        return 100 * (koef < 0 ? 0 : koef);
    }
    /*
    public PredictionInfo createVersion2(Match m) {
        PredictionInfo predictionInfo = new PredictionInfo();
        predictionInfo.setMatchId(m.getId());
        predictionInfo.setTeam1(dbInfo.teamService.getById(m.getIdTeam1()).getName());
        predictionInfo.setTeam2(dbInfo.teamService.getById(m.getIdTeam2()).getName());
        return createPredictVersion2(predictionInfo, m.getDate());
    }

    private PredictionInfo createPredictVersion2(PredictionInfo pi, Date currentDate) {
        double points1;
        double points2;
        lastPredict = pi;

        points1 = getPointsOfTeamVersion2(pi.getTeam1(), currentDate);
        pi.setPoints1(points1);
        points2 = getPointsOfTeamVersion2(pi.getTeam2(), currentDate);
        pi.setPoints2(points2);

        pi.setFirstWin(points1 > points2);
        return pi;
    }

    private double getPointsOfTeamVersion2(String teamName, Date currentDate) {
        Team team = dbInfo.teamService.getByName(teamName);
        List<Match> matches = dbInfo.matchService.getByTeamIdOrderByDate(team.getId());

        double allPoints = 0;
        for (Match m: matches) {
            long timeDifference = currentDate.getTime() - m.getDate().getTime();
            if (timeDifference > week * 3) {
                continue;
            }


        }
    }
*/

}

class PredictionException extends Exception {
    public PredictionException(String message){
        super(message);
    }
}
