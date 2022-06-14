package com.program.WhoWins.service;

import com.program.WhoWins.entity.Prediction;
import com.program.WhoWins.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    PredictionRepository predictionRepository;

    @Override
    public Prediction addPrediction(Prediction prediction) {
        return predictionRepository.saveAndFlush(prediction);
    }

    @Override
    public void setCorrect(long id, boolean ended, boolean correct) {
        predictionRepository.setCorrect(id, ended, correct);
    }

    @Override
    public Prediction getByMatchId(long matchId) {
        return predictionRepository.getByMatchId(matchId);
    }

    @Override
    public List<Prediction> getNotEnded() {
        return predictionRepository.getNotEnded();
    }


}
