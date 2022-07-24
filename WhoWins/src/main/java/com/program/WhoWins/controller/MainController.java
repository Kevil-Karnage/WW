package com.program.WhoWins.controller;

import com.program.WhoWins.entity.*;
import com.program.parsing.utils.DataParsing;
import com.program.parsing.utils.PredictionConverter;
import com.program.prediction.PredictionGenerator;
import com.program.shell.DatabaseInfo;
import com.program.shell.ParsingInfo;
import com.program.shell.PredictionInfo;
import com.program.shell.RatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
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
        Match result = dataParsing.parseOneResult();
        model.addAttribute("result", result);
        List<Map> maps = dbInfo.mapService.getByMatchId(result.getId());
        model.addAttribute("maps", maps);
        return "result";
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

    @GetMapping("/match/{id}")
    public String match(@PathVariable(name = "id") long matchId, Model model) {
        Match m = dbInfo.matchService.getById(matchId);
        if (m == null) {
            model.addAttribute("match", null);
            return "match";
        }

        model.addAttribute("match", m);
        PredictionGenerator pg = new PredictionGenerator(dbInfo);

        PredictionInfo prediction1 = pg.create(m);
        model.addAttribute("prediction1", prediction1);
        PredictionInfo prediction2 = pg.createV2(m);
        model.addAttribute("prediction2", prediction2);
        return "match";
    }

    @GetMapping("/predictions")
    public String predictions(Model model) {
        PredictionGenerator pg = new PredictionGenerator(dbInfo);

        List<Match> matches = dbInfo.matchService.getMatchesByEndedValue(false);
        //List<PredictionInfo> predictions = makePredictArray(matches, pg, false);
        //model.addAttribute("predictions", predictions);

        //List<PredictionInfo> expPredictions = makePredictArray(matches, pg, true);
        //model.addAttribute("experiment", expPredictions);


        return "predictions";
    }

    @GetMapping("/saveAndPredict")
    public String allPredictions(Model model) throws Exception {
        PredictionGenerator pg = new PredictionGenerator(dbInfo);
        DataParsing dp = new DataParsing(dbInfo);

        List<Match> matches = dp.parseMatches(0);
        //List<PredictionInfo> predictions = makePredictArray(matches, pg, false);
        //model.addAttribute("predictions", predictions);
        return "predictions";
    }
/*
    private List<PredictionInfo> makePredictArray (List<Match> matches, PredictionGenerator pg, boolean experimental) {
        PredictionConverter pc = new PredictionConverter(dbInfo);
        List<Prediction> fromDB = dbInfo.predictionService.getNotEnded();
        List<PredictionInfo> predictions = pc.convertToListPredictionInfo(fromDB);
        for (Match m: matches) {
            PredictionInfo newPrediction;
            if (experimental) {
                newPrediction = pg.createV2(m);
            } else {
                newPrediction = pg.create(m);
            }

            if (newPrediction != null)
                predictions.add(newPrediction);
        }

        return predictions;
    }
*/
    @GetMapping("/rating")
    public String getRating(Model model) {
        PredictionGenerator pg = new PredictionGenerator(dbInfo);
        List<RatingInfo> rating = pg.getRating();
        model.addAttribute("rating", rating);
        return "rating";
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<Event> events = dbInfo.eventService.getAll();
        model.addAttribute("events", events);
        return "events";
    }

}