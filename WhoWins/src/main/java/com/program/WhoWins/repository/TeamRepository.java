package com.program.WhoWins.repository;

import com.program.WhoWins.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select b from Team b where b.name = :name")
    Team getByName(String name);
}
