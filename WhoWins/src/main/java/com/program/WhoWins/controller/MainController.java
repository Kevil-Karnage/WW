package com.program.WhoWins.controller;

import com.program.WhoWins.entity.*;
import com.program.parsing.utils.DataParsing;
import com.program.parsing.utils.PredictionConverter;
import com.program.prediction.PredictionGenerator;
import com.program.shell.DatabaseInfo;
import com.program.shell.ParsingInfo;
import com.program.shell.PredictionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    DatabaseInfo dbInfo;

    @GetMapping("/teams")
    public String teams(Model model) {

        List<Team> teams = dbInfo.teamService.getAll();
        model.addAttribute("teams", teams);

        return "teams";
    }

    @GetMapping("/saveAllMatches/{number}")
    public String saveAllMatches(Model model, @PathVariable(name = "number") int numberOfDay) throws Exception {
        DataParsing dataParsing = new DataParsing(dbInfo);

        List<Match> matches = dataParsing.parseMatches(numberOfDay);
        model.addAttribute("matches", matches);
        return "matches";
    }

    @GetMapping("/saveAllResults/{count}")
    public String saveAllResults(Model model, @PathVariable(name = "count") int count) throws Exception {
        DataParsing dataParsing = new DataParsing(dbInfo);

        ParsingInfo<Match> parsingInfo = dataParsing.parseResults(count);
        model.addAttribute("parsing", parsingInfo);
        return "results";
    }


    @GetMapping("/saveLastResult")
    public String saveLastResult(Model model) throws Exception {
        DataParsing dataParsing = new DataParsing(dbInfo);
        Match match = dataParsing.parseOneResult();
        model.addAttribute("match", match);
        return "home";
    }

    @PostMapping("/prediction")
    public String prediction(@RequestParam("team1") String team1,
                             @RequestParam("team2") String team2,
                             Model model) {
        PredictionGenerator predictionGenerator = new PredictionGenerator(dbInfo);
        PredictionInfo prediction = predictionGenerator.create(team1, team2, new Date());
        model.addAttribute("prediction", prediction);
        return "prediction";
    }

    @GetMapping("/predictions")
    public String predictions(Model model) {
        PredictionGenerator pg = new PredictionGenerator(dbInfo);

        List<Match> matches = dbInfo.matchService.getMatchesByEndedValue(false);
        List<PredictionInfo> predictions = makePredictArray(matches, pg);

        model.addAttribute("predictions", predictions);
        return "predictions";
    }

    @GetMapping("/saveAndPredict")
    public String allPredictions(Model model) throws Exception {
        PredictionGenerator pg = new PredictionGenerator(dbInfo);
        DataParsing dp = new DataParsing(dbInfo);

        List<Match> matches = dp.parseMatches(0);
        List<PredictionInfo> predictions = makePredictArray(matches, pg);
        model.addAttribute("predictions", predictions);
        return "predictions";
    }

    private List<PredictionInfo> makePredictArray (List<Match> matches, PredictionGenerator pg) {
        PredictionConverter pc = new PredictionConverter(dbInfo);
        List<Prediction> fromDB = dbInfo.predictionService.getNotEnded();
        List<PredictionInfo> predictions = pc.convertToListPredictionInfo(fromDB);
        for (Match m: matches) {
            PredictionInfo newPrediction = pg.create(m);
            if (newPrediction != null) {
                /*Prediction predict = dbInfo.savePrediction(newPrediction);
                if (predict != null)*/
                predictions.add(newPrediction);
            }
        }

        return predictions;
    }


}