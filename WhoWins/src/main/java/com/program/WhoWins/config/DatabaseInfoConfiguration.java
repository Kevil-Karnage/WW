package com.program.WhoWins.config;

import com.program.WhoWins.service.*;
import com.program.shell.DatabaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatabaseInfoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInfoConfiguration.class);
    @Bean
    DatabaseInfo initDatabaseInfo() {
        log.info("DatabaseInfo initialization");
        return new DatabaseInfo(
                new MatchServiceImpl(),
                new MapServiceImpl(),
                new PlayedMapServiceImpl(),
                new TeamServiceImpl(),
                new EventServiceImpl(),
                new PredictionServiceImpl());
    }
}
