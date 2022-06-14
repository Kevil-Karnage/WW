package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    @Modifying
    @Query("update Prediction b " +
            "set b.ended = :ended, " +
            "b.correct = :correct " +
            "where b.id = :id")
    void setCorrect(long id, boolean ended, boolean correct);
    @Query("select b " +
            "from Prediction b " +
            "where b.matchId = :matchId")
    Prediction getByMatchId(long matchId);
    @Query("select b from Prediction b where b.ended = false")
    List<Prediction> getNotEnded();
}
