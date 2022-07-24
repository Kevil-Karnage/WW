package com.program.prediction;

import com.program.WhoWins.entity.MapType;
import com.program.WhoWins.entity.Match;
import com.program.WhoWins.entity.Map;
import com.program.WhoWins.entity.Team;
import com.program.shell.DatabaseInfo;
import com.program.shell.PredictionInfo;
import com.program.shell.RatingInfo;

import java.util.*;

public class PredictionGenerator {

    long week = 604800000;

    DatabaseInfo dbInfo;

    PredictionInfo lastPredict;
    RatingInfo lastRatingInfo;

    public PredictionGenerator(DatabaseInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public List<RatingInfo> getRating() {
        List<Team> teams = dbInfo.teamService.getAll();
        List<RatingInfo> ratingPoints = new ArrayList<>();

        Date date = new Date();
        for (int i = 0; i < teams.size(); i++) {
            String teamName = teams.get(i).getName();
            lastRatingInfo = new RatingInfo();
            try {
                setTeamPoints(teamName, date, true, 3);
            } catch (PredictionException e) {
                System.out.println(e);
            }
            if (lastRatingInfo.getPoints() != 0) {
                lastRatingInfo.setName(teamName);
            }
            ratingPoints.add(lastRatingInfo);
        }
        ratingPoints.sort(new Comparator<RatingInfo>() {
            @Override
            public int compare(RatingInfo o1, RatingInfo o2) {
                if (o1.getPoints() > o2.getPoints()) {
                    return -1;
                }
                if (o1.getPoints() < o2.getPoints()) {
                    return 1;
                }

                return 0;
            }
        });

        return ratingPoints;
    }

    public PredictionInfo create(String team1, String team2, Date dateOfMatch) {
        lastPredict = new PredictionInfo();
        lastPredict.setTeam1(team1);
        lastPredict.setTeam2(team2);
        if (dbInfo.teamService.getByName(team1) == null ||
                dbInfo.teamService.getByName(team2) == null) {
            return null;
        }
        try {
            createPredict(dateOfMatch);
        } catch (PredictionException e) {
            System.out.println(e);
            return null;
        }
        return lastPredict;
    }

    public PredictionInfo create(Match m) {
        // создаём новый предикт
        lastPredict = new PredictionInfo();
        // сохраняем информацию о нём
        lastPredict.setMatchId(m.getId());
        lastPredict.setTeam1(dbInfo.teamService.getById(m.getIdTeam1()).getName());
        lastPredict.setTeam2(dbInfo.teamService.getById(m.getIdTeam2()).getName());
        // получаем результат
        try {
            createPredict(m.getDate());
        } catch (PredictionException e) {
            System.out.println(e);
            return null;
        }
        // возвращаем результат
        return lastPredict;
    }

    private void createPredict(Date dateOfMatch) throws PredictionException {

        setTeamPoints(lastPredict.getTeam1(), dateOfMatch, false, 3);
        if (lastPredict.getPoints1() < 0.1) {
            lastPredict.setType(5);
            setTeamPoints(lastPredict.getTeam1(), dateOfMatch, false, 5);
        }
        setTeamPoints(lastPredict.getTeam2(), dateOfMatch, false, 3);
        if (lastPredict.getPoints2() < 0.1) {
            lastPredict.setType(5);
            setTeamPoints(lastPredict.getTeam2(), dateOfMatch, false, 5);
        }

        if (lastPredict.getPoints1() <= 0 || lastPredict.getPoints2() <= 0) {
            lastPredict.setType(1);
            System.out.println("У команды нет очков");
        }

        lastPredict.setFirstWin(lastPredict.getPoints1() > lastPredict.getPoints2());
    }


    /**
     * сохранение очков команд в прогноз
     * @param teamName
     * @param date
     * @throws PredictionException
     */
    private void setTeamPoints(String teamName, Date date, boolean forRating, int maxCountWeek) throws PredictionException {
        // получаем инфу о команде и находим её матчи
        Team team = dbInfo.teamService.getByName(teamName);
        List<Match> matches = dbInfo.matchService.getEndedByTeamIdOrderByDate(team.getId());

        // если нет сохранённых матчей, то кидаем исключение
        if (matches.size() < 3) {
            lastPredict.setType(2);
            System.out.println("V1: Слишком мало матчей");
        }
        // создаём список очков для карт
        java.util.Map mapPoints = new HashMap<>();

        int countMatterMatches = 0;

        for (Match m : matches) {
            // находим коэффициент матча
            double koef = getKoefForTimeAndRating(m, date, maxCountWeek);
            if ((int) koef == 0) {
                continue;
            }
            // узнаём, под каким номером искомая команда, 1 или 2
            boolean first = m.getIdTeam1().equals(team.getId());
            // получаем сыгранные карты матча
            List<Map> maps = dbInfo.mapService.getByMatchId(m.getId());
            // сохраняем в список очков для карт
            for (Map pm : maps) {
                try {
                    addPointsOfPlayedMap(mapPoints, pm, koef, first);
                } catch (PredictionException e) {
                    continue;
                }
            }
            countMatterMatches++;
        }
        // если было учтено хотя бы 3 матча, то
        if (countMatterMatches < 3) {
            lastPredict.setType(2);
        }


        boolean isFirstTeam = false;
        if (!forRating) {
            // сохраняем все найденные очки в прогноз:
            isFirstTeam = lastPredict.getTeam1().equals(teamName);
        }
        setPointsToPredict(mapPoints, isFirstTeam, forRating);
    }

    /**
     * сохранение очков команды - по каждому типу карт и в сумме
     * @param mapPointsDictionary
     * @param first
     */
    private void setPointsToPredict(java.util.Map<String, List<Double>> mapPointsDictionary, boolean first, boolean forRating) {
        java.util.Map<String, Double> mapsPoints = new HashMap<>();

        double allPoints = 0;
        double countMaps = 0;

        for (String mapType : mapPointsDictionary.keySet()) {
            // получаем очки за сыгранные карты данного типа
            List<Double> points = mapPointsDictionary.get(mapType);
            // и количество сыгранных карт
            countMaps += points.size();

            // получаем сумму очков за сыгранные карты данного типа
            double mapTypePoints = 0;
            for (Double p : points) {
                mapTypePoints += p;
            }

            // сохраняем в очки типов карт
            mapsPoints.put(mapType, mapTypePoints / points.size());
            // сохраняем в общие очки
            allPoints += mapTypePoints;
        }

        double points = allPoints / countMaps;

        if (forRating) {
            lastRatingInfo.setPoints(points);
        } else {
            // сохраняем всё в прогноз
            if (first) {
                lastPredict.setMapsPoints1(mapsPoints);
                lastPredict.setPoints1(points);
            } else {
                lastPredict.setMapsPoints2(mapsPoints);
                lastPredict.setPoints2(points);
            }
        }
    }


    /**
     * добавление очков за сыгранную карту
     * @param mapPointsDictionary
     * @param map
     * @param koef
     * @param first
     */
    private void addPointsOfPlayedMap(java.util.Map<String, List<Double>> mapPointsDictionary,
                                      Map map, double koef, boolean first) throws PredictionException {
        // получаем очки за карту
        double pointsOfCurrentMap = getPointsOfMap(map, koef, first);
        if (pointsOfCurrentMap < 1) {
            throw new PredictionException("Карта не важна: очки за карту меньше или равны 0");
        }
        // получаем инфу о карте
        MapType mapTypeEntity = dbInfo.mapTypeService.getById(map.getMapTypeId());
        String mapKey = mapTypeEntity.getName();

        if (mapKey.equals("Default")) {
            throw new PredictionException("Карта не важна: Карта не входит в основной маппул");
        }

        List<Double> points;
        // если она была добавлена, то обновляем инфу
        if (mapPointsDictionary.containsKey(mapKey)) {
            // получаем список очков за сыгранные карты этого типа
            points = mapPointsDictionary.get(mapKey);
        } else {
            // а если нет, то создаём список для этого типа карты
            points = new ArrayList<>();
        }
        // добавляем очки за эту карту
        points.add(pointsOfCurrentMap);
        // сохраняем инфу в словарь
        mapPointsDictionary.put(mapKey, points);
    }

    /**
     * получаем очки за сыгранную карту (с учётом коэффициентов)
     * @param pm
     * @param koef
     * @param first
     * @return
     */
    private double getPointsOfMap(Map pm, double koef, boolean first) {
        double mapPoints = first ?
                16 + pm.getScoreTeam1() - pm.getScoreTeam2() :
                16 + pm.getScoreTeam2() - pm.getScoreTeam1();
        return mapPoints * koef;
    }

    /**
     * получаем коэффициент времени и рейтинга для карт матча
     * @param m
     * @param date
     * @return
     */
    private double getKoefForTimeAndRating(Match m, Date date, int maxCountWeek) {
        long timeDifference = date.getTime() - m.getDate().getTime();

        double timeKoef = 100 * getTimeKoef(timeDifference, maxCountWeek);
        double ratingKoef = 100 * getRatingKoef(m.getPositionHLTV1(), m.getPositionHLTV2());
        if (timeKoef == 0 || ratingKoef == 0) {
            return 0;
        }
        return timeKoef + ratingKoef;
    }

    private double getRatingKoef(int hltvPos1, int hltvPos2) {
        double koef;

        MatchClass firstClass = MatchClass.indexOf(hltvPos1);
        MatchClass secondClass = MatchClass.indexOf(hltvPos2);

        koef = firstClass.rank + secondClass.rank;

        return koef;
    }

    private double getTimeKoef(long time, int maxCountWeeks) {
        long countWeeks = time / week;
        double koef = 1 - (1.0 / maxCountWeeks * countWeeks);
//        double koef = 1 - (0.33 * countWeeks);
//        double koef = 1 - (0.2 * countWeeks);
        if (koef < 0.1) {
            return 0;
        }
        return koef;
    }

    public PredictionInfo createV2(Match m) {
        PredictionInfo predictionInfo = new PredictionInfo();
        predictionInfo.setMatchId(m.getId());
        predictionInfo.setTeam1(dbInfo.teamService.getById(m.getIdTeam1()).getName());
        predictionInfo.setTeam2(dbInfo.teamService.getById(m.getIdTeam2()).getName());
        lastPredict = predictionInfo;
        return createPredictVersion2(m);
    }

    private PredictionInfo createPredictVersion2(Match m) {
        try {
            getPointsOfTeamVersion2(m, true);
            getPointsOfTeamVersion2(m, false);
        } catch (PredictionException e) {
            System.out.println(e);
            return null;
        }

        lastPredict.setFirstWin(lastPredict.getPoints1() > lastPredict.getPoints2());
        return lastPredict;
    }

    private void getPointsOfTeamVersion2(Match original, boolean first) throws PredictionException {
        double defaultMatchPoints = 1000;
        long teamId = first ? original.getIdTeam1() : original.getIdTeam2();
        List<Match> matches = dbInfo.matchService.getEndedByTeamIdOrderByDate(teamId);

        double allPoints = 0;
        int countMaps = 0;

        int countMatches = 0;

        java.util.Map mapsPoints = new HashMap<>();

        for (Match m: matches) {
            long timeDifference = original.getDate().getTime() - m.getDate().getTime();
            if (!m.isEnded() || timeDifference > week * 3) {
                continue;
            }
            List<Map> maps = dbInfo.mapService.getByMatchId(m.getId());
            countMaps += maps.size();

            double koef = getRatingKoef(m.getPositionHLTV1(), m.getPositionHLTV2());
            double matchPoints = getPointsOfMatch(maps, first, defaultMatchPoints * koef);
            getPointsOfMaps(maps, first, defaultMatchPoints * koef);

            allPoints += matchPoints;
            countMatches++;
        }

        if (countMatches < 3) {
            throw new PredictionException("Слишком мало матчей");
        }

        double points = allPoints / countMaps;
        if (first) {
            lastPredict.setPoints1(points);
            lastPredict.setMapsPoints1(mapsPoints);
        } else {
            lastPredict.setPoints2(points);
            lastPredict.setMapsPoints2(mapsPoints);
        }
    }

    private void getPointsOfMaps(List<Map> maps, boolean firstTeam, double koef) {
        java.util.Map<String, List<Double>> mapsPoints = new HashMap<>();

        for (Map pm : maps) {
            double teamRounds;
            double enemyRounds;

            if (firstTeam) {
                teamRounds = pm.getScoreTeam1();
                enemyRounds = pm.getScoreTeam2();
            } else {
                teamRounds = pm.getScoreTeam2();
                enemyRounds = pm.getScoreTeam1();
            }

            double pmPoint = getPoints(teamRounds, enemyRounds, koef);
            if (pmPoint < 1) {
                continue;
            }
            MapType mapTypeName = dbInfo.mapTypeService.getById(pm.getMapTypeId());
            List<Double> mapTypePoints;

            if (mapsPoints.containsKey(mapTypeName.getName())) {
                mapTypePoints = mapsPoints.get(mapTypeName.getName());
            } else {
                mapTypePoints = new ArrayList<>();
            }

            mapTypePoints.add(pmPoint);
            mapsPoints.put(mapTypeName.getName(), mapTypePoints);
        }

        java.util.Map<String, Double> points = new HashMap<>();
        for (String mapType : mapsPoints.keySet()) {
            List<Double> fromMap = mapsPoints.get(mapType);
            double avgMapTypePoints = sumDoubleList(fromMap) / fromMap.size();
            points.put(mapType, avgMapTypePoints);
        }
        if (firstTeam) {
            lastPredict.setMapsPoints1(points);
        } else {
            lastPredict.setMapsPoints2(points);
        }
    }

    private double sumDoubleList(List<Double> list) {
        double sum = 0;
        for (double d : list) {
            sum += d;
        }
        return sum;
    }

    private double getPointsOfMatch(List<Map> maps, boolean firstTeam, double koef) {
        double teamRounds = 0;
        double enemyRounds = 0;

        for (Map pm : maps) {
            if (firstTeam) {
                teamRounds += pm.getScoreTeam1();
                enemyRounds += pm.getScoreTeam2();
            } else {
                teamRounds += pm.getScoreTeam2();
                enemyRounds += pm.getScoreTeam1();

            }
        }
        return getPoints(teamRounds, enemyRounds, koef);
    }

    private double getPoints(double teamRounds, double enemyRounds, double koef) {
        return teamRounds / (teamRounds + enemyRounds) * koef;
    }
}

class PredictionException extends Exception {
    public PredictionException(String message){
        super(message);
    }
}
