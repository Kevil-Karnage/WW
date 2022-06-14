package com.program.WhoWins.service;

import com.program.WhoWins.entity.Prediction;

import java.util.List;

public interface PredictionService {
    Prediction addPrediction(Prediction prediction);
    void setCorrect(long id, boolean ended, boolean correct);
    Prediction getByMatchId(long matchId);
    List<Prediction> getNotEnded();
}
