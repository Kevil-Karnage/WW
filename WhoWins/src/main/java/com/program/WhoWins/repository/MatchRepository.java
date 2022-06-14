package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("select b from Match b where b.idTeam1 = :teamId or b.idTeam2 = :teamId")
    List<Match> getByTeamId(long teamId);
    @Query("select b from Match b where b.idTeam1 = :teamId or b.idTeam2 = :teamId order by b.date DESC")
    List<Match> getByTeamIdOrderByDate(long teamId);
    @Query("select b from Match b where b.eventId = :eventId")
    List<Match> getByEventId(long eventId);
    @Query("select b from Match b where b.eventId = :eventId and b.idTeam1 in (:idTeam1, :idTeam2) and b.idTeam2 in (:idTeam1, :idTeam2) and b.date = :date")
    Match getByEventTeamsAndDate(long eventId, long idTeam1, long idTeam2, Date date);
    @Modifying
    @Query("update Match b set b.ended = :ended where b.id = :id")
    void setEnded(long id, boolean ended);
    @Query("select b from Match b where b.ended = :ended")
    List<Match> getMatchesByEndedValue(boolean ended);
}
