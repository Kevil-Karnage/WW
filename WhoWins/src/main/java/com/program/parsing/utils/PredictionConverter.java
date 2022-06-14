package com.program.parsing.utils;

import com.program.WhoWins.entity.Match;
import com.program.WhoWins.entity.Prediction;
import com.program.WhoWins.entity.Team;
import com.program.shell.DatabaseInfo;
import com.program.shell.PredictionInfo;

import java.util.ArrayList;
import java.util.List;

public class PredictionConverter {
    DatabaseInfo dbInfo;

    public PredictionConverter(DatabaseInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public List<PredictionInfo> convertToListPredictionInfo(List<Prediction> predictionList) {
        List<PredictionInfo> predictionInfoList = new ArrayList<>();

        for (Prediction p : predictionList) {
            try {
                PredictionInfo pi = new PredictionInfo();
                pi.setMatchId(p.getMatchId());
                Match m = dbInfo.matchService.getById(p.getMatchId());

                Team team1 = dbInfo.teamService.getById(m.getIdTeam1());
                pi.setTeam1(team1.getName());
                Team team2 = dbInfo.teamService.getById(m.getIdTeam2());
                pi.setTeam2(team2.getName());
                pi.setFirstWin(p.getWinnerId().equals(m.getIdTeam1()));


                pi.setPoints1(p.getPoints1());
                pi.setPoints2(p.getPoints2());

                predictionInfoList.add(pi);
            } catch (Exception e) {
                continue;
            }
        }
        return predictionInfoList;
    }
}
