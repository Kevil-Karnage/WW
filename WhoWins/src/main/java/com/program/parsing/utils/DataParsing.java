package com.program.parsing.utils;

import com.program.WhoWins.entity.*;
import com.program.shell.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DataParsing {

    private DatabaseInfo dbInfo;
    private static int connectsCount = 0;
    private String teamNamePattern = "[\\w\\s'.-]*";
    private String hostbase = "https://hltv.org%s";

    public DataParsing(DatabaseInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    /**
     * получаем матчи со страницы результатов
     * @param doc
     * @return
     */
    public Element getResultsInfo(Document doc, boolean onlyLastDay) {
        // все результаты матчей со страницы
        Elements allResults = doc.select("div.allres");
        if (onlyLastDay) {
            // результаты сегодняшнего дня
            return allResults.select("div.results-sublist").get(0);
        }

        return allResults.get(0);
    }

    /**
     * parse html document to Document.java
     * @param link
     * @return
     */
    public Document getHTMLDocument(String link) throws InterruptedException, IOException {
        Document doc = null;
        while (doc == null) {
            if (connectsCount % 10 == 0) {
                System.out.println("Pause: 5 seconds");
                Thread.sleep(5000);
            }
            if (connectsCount % 50 == 0) {
                System.out.println("Pause: 10 seconds");
                Thread.sleep(10000);
            }
            try {
                connectsCount++;
                System.out.println("Connect to HLTV.org: " + connectsCount);
                doc = Jsoup.connect(link)
                        .userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com")
                        .get();

            } catch (SocketTimeoutException e) {
                System.out.println(e);
            }
        }
        return doc;
    }

    /**
     * get link from element from href in cssQuery
     * (if cssQuery is null, then without it)
     * @param el
     * @param cssQuery
     * @return
     */
    public String getLinkFromHref(Element el, String cssQuery) {
        if (cssQuery == null) {
            return String.format(hostbase, el.attr("href"));
        }
        return String.format(hostbase, el.select(cssQuery).attr("href"));
    }

    public List<Match> parseMatches(int numberOfDay) throws IOException, InterruptedException, ParseException, DataParsingException {
        Document doc = getHTMLDocument("https://www.hltv.org/matches");
        Element futureMatchesElement = doc.select("div.upcomingMatchesSection").get(numberOfDay);
        String dateString = futureMatchesElement.select("span.matchDayHeadline").text();
        dateString = dateString.split(" ")[2] + " - ";

        Elements allMatchesElements = futureMatchesElement.select("div.upcomingMatch");
        Elements allLinksOfMatchesElements = futureMatchesElement.select("a.a-reset");

        List<Match> matches = new ArrayList<>();
        System.out.println("Loading MATCHES: 0 from " + allMatchesElements.size());
        for (int i = 0; i < allMatchesElements.size(); i++) {
            System.out.println((i + 1) + " from " + allMatchesElements.size());

            Element currentElement = allMatchesElements.get(i);
            String currentMatchLink = getLinkFromHref(allLinksOfMatchesElements.get(i), null);

            MatchInfo matchInfo = new MatchInfo();

            parseMainMatchInfo(matchInfo, currentElement, dateString);
            // если что-то не так с инфой, не сохраняем матч
            try {
                parseMatchLink(currentMatchLink, matchInfo);
            } catch (DataParsingException e) {
                System.out.println("Loading MATCHES: Skipped (Exception)");
                continue;
            }

            // сохраняем матч в бд
            Match match = dbInfo.saveMatch(matchInfo, false);

            // если матч сохранён, то добавляем в список сохранённых
            if (match != null) matches.add(match);
        }

        System.out.println("Loading MATCHES: Complete");
        return matches;
    }

    public Match parseOneResult() throws IOException, InterruptedException, ParseException {
        Document doc = getHTMLDocument(String.format(hostbase, "/results"));
        Element resultsElement = getResultsInfo(doc, false);

        // результаты матчей
        Elements allMatchesElements = resultsElement.select("div.result-con");
        // ссылки на матчи
        Elements allLinksOfMatchesElements = resultsElement.select("a.a-reset");

        Element matchElement = allMatchesElements.get(0);
        String matchLinkElement = getLinkFromHref(allLinksOfMatchesElements.get(0), null);
        // создаём матч
        MatchInfo newMatch;
        try {
            newMatch = parseMatchInfo(matchElement);
            // парсим страницу матча
            parseMatchLink(matchLinkElement, newMatch);

        } catch (DataParsingException e) {
            return null;
        }

        return dbInfo.saveMatch(newMatch, true);
    }

    public ParsingInfo<Match> parseResults(int count) throws IOException, ParseException, InterruptedException {
        String shortLink = "https://www.hltv.org/results";
        String longLink = "https://www.hltv.org/results?offset=";
        ParsingInfo<Match> results = new ParsingInfo<>();
        if (count <= 100) {
            return findResults(shortLink, results);
        }
        int countCycles = count / 100 - 1;
        System.out.println("Loading RESULTS - part 1 from " + countCycles);
        results = findResults(shortLink, results);
        for (int i = 1; i < countCycles; i++) {
            System.out.println("Loading RESULTS - part " + i + " from " + countCycles);
            results = findResults(longLink + (i * 100), results);
        }
        System.out.println("Loading RESULTS - Complete");
        return results;
    }

    public ParsingInfo<Match> findResults(String link, ParsingInfo<Match> parsingInfo) throws IOException, InterruptedException, ParseException {
        Document doc = null;
        try {
            while (doc == null) {
                doc = getHTMLDocument(link);
            }
        } catch (SocketTimeoutException e){
            System.out.println(e);
            return null;
        }
        Element resultsElement = getResultsInfo(doc, false);
        // результаты матчей
        Elements allMatchesElements = resultsElement.select("div.result-con");
        // ссылки на матчи
        Elements allLinksOfMatchesElements = resultsElement.select("a.a-reset");

        parsingInfo.found += allMatchesElements.size();

        parsingInfo.result = new ArrayList<>();
        for (int i = 0; i < allMatchesElements.size(); i++) {
            if (i % (allMatchesElements.size() / 10) == 0) {
                System.out.println("Loading RESULTS: " + i + "%");
            }
            Element matchElement = allMatchesElements.get(i);
            String matchLink = getLinkFromHref(allLinksOfMatchesElements.get(i), null);

            parseResultOfMatch(parsingInfo, matchElement, matchLink);
        }
        System.out.println("Loading RESULTS: 100%");
        return parsingInfo;
    }

    private void parseResultOfMatch(ParsingInfo<Match> pi, Element matchElement, String matchLink) {
        // создаём матч
        MatchInfo newMatch;
        try {
            // собираем информацию о матче со страницы результатов
            newMatch = parseMatchInfo(matchElement);
            // собираем информацию со страницы матча
            newMatch = parseMatchLink(matchLink, newMatch);
        } catch (DataParsingException | IOException | InterruptedException | ParseException e) {
            // если что-то собрать не получилось, отмечаем это и переходим к следующему матчу
            System.out.println(e);
            pi.failed++;
            return;
        }
        if (newMatch == null) {
            pi.failed++;
            return;
        }

        // сохраняем матч
        Match match = dbInfo.saveMatch(newMatch, true);
        if (match != null) {
            // если получилось, то добавляем в результат
            pi.result.add(match);
            pi.parsed++;
        } else {
            // иначе матч уже был добавлен ранее, отмечаем это
            pi.alreadyAdded++;
        }
    }

    private void parseMainMatchInfo(MatchInfo matchInfo, Element el, String dateString) throws ParseException, DataParsingException {
        String time = dateString + el.select("div.matchTime").text();
        matchInfo.date = DateConverter.stringToDateTime(time);
        matchInfo.matchType = Integer.parseInt("" + el.select("div.matchMeta").text().charAt(2));

        String team1 = el.select("div.team1").text();
        String team2 = el.select("div.team2").text();

        if (!(
                Pattern.matches(teamNamePattern, team1) &&
                        Pattern.matches(teamNamePattern, team2)
        )) {
            throw new DataParsingException("Неверные названия команд");
        }
        matchInfo.team1 = team1;
        matchInfo.team2 = team2;

        matchInfo.stars = el.select("div.fa.fa-star").size();
    }

    private MatchInfo parseMatchInfo(Element matchElement) throws DataParsingException {
        // создаём матч
        MatchInfo matchInfo = new MatchInfo();
        // сохраняем что он закончился
        matchInfo.ended = true;
        // получаем уровень матча (звёздность)
        matchInfo.stars = matchElement.select("i.fa.fa-star.star").size();

        // получаем команды матча
        Elements teamsElement = matchElement.select("div.team");
        String team1 = teamsElement.get(0).text();
        String team2 = teamsElement.get(1).text();
        // если названия команд не соответствуют норме, значит с матчем что-то не так, бросаем исключение
        if (Pattern.matches(teamNamePattern, team1) && Pattern.matches(teamNamePattern, team2)) {
            matchInfo.team1 = team1;
            matchInfo.team2 = team2;
            return matchInfo;
        } else {
            throw new DataParsingException("Некорректные названия команд");
        }
    }

    public MatchInfo parseMatchLink(String matchLink, MatchInfo matchInfo)
            throws IOException, InterruptedException, DataParsingException, ParseException {
        // получаем содержимое ссылки
        Document doc = getHTMLDocument(matchLink);
        if (doc == null) {
            return null;
        }

        // получаем id матча на хлтв
        parseHLTVMatchId(matchInfo, matchLink);
        // парсим страницу турнира
        parseEventLink(doc, matchInfo);
        // получаем дату матча
        parseDate(doc, matchInfo);
        // получаем позиции команд на HLTV
        Elements positionsOnRankingElement = doc.select("div.teamRanking");
        matchInfo.positionHLTV1 = parseHLTVPositions(positionsOnRankingElement, true);
        matchInfo.positionHLTV2 = parseHLTVPositions(positionsOnRankingElement, false);

        // получаем имена игроков
        parsePlayersNames(doc, matchInfo);

        // получаем сыгранные в матче карты
        matchInfo.maps = parseMaps(doc);
        // получаем тип матча (бо1, бо3, бо5)
        matchInfo.matchType = matchInfo.maps.length;

        return matchInfo;
    }

    private void parseHLTVMatchId(MatchInfo matchInfo, String matchLink) {
        String hltvId = matchLink.split("/")[4];
        matchInfo.hltvId = Long.parseLong(hltvId);
    }

    private void parseEventLink(Element doc, MatchInfo matchInfo) throws IOException, InterruptedException {
        Element eventLinkElement = doc.select("div.event").get(0);
        String link = getLinkFromHref(eventLinkElement, "a");
        Document eventDoc = getHTMLDocument(link);

        EventInfo eventInfo = new EventInfo();

        // сохраняем название турнира
        String name = eventDoc.select("h1.event-hub-title").text();
        eventInfo.setName(name);

        // получаем даты начала и конца турнира
        Elements dates = eventDoc.select("td.eventdate").get(0).select("span");
        List<String> stringDates = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String date =  dates.get(i).attr("data-unix");
            if (!date.equals("")) {
                stringDates.add(date);
            }
        }
        // сохраняем найденные даты
        long beginDate = Long.parseLong(stringDates.get(0));
        eventInfo.setBeginDate(new java.sql.Date(beginDate));

        long endDate = beginDate;
        if (stringDates.size() > 1) {
            endDate = Long.parseLong(stringDates.get(1));
        }

        eventInfo.setEndDate(new java.sql.Date(endDate));

        // добавляем турнир в матч
        matchInfo.event = eventInfo;
    }

    private MapInfo[] parseMaps(Document doc) throws IOException, InterruptedException {
        Elements mapsElement = doc.select("div.mapholder");
        MapInfo[] mapsInfo = new MapInfo[mapsElement.size()];
        for (int i = 0; i < mapsInfo.length; i++) {
            Element mapElement = mapsElement.get(i);
            // сохраняем название карты (или TBA)
            MapInfo mapInfo = new MapInfo();
            mapInfo.map = mapElement.select("div.mapname").text();
            // сохраняем счет карты
            Elements scoresElement = mapElement.select("div.results-team-score");
            if (scoresElement.size() != 0) {
                if (!scoresElement.get(0).text().equals("-")) {
                    mapInfo.score1 = Integer.parseInt(scoresElement.get(0).text());
                    mapInfo.score2 = Integer.parseInt(scoresElement.get(1).text());
                }
                if (!(mapInfo.score1 == 0 && mapInfo.score2 == 0)) {
                    mapsInfo[i] = mapInfo;
                }
            }
        }
        return mapsInfo;
    }

    private void parsePlayersNames(Document doc, MatchInfo matchInfo) {
        Elements playersTablesElement = doc.select("table.totalstats");
        // получаем имена игроков из общей таблицы
        parsePlayersNameTable(playersTablesElement.get(0), matchInfo, true);
        parsePlayersNameTable(playersTablesElement.get(1), matchInfo, false);
    }

    private void parsePlayersNameTable(Element playerTableElement, MatchInfo matchInfo, boolean first) {
        Elements playersElements = playerTableElement.select("div.gtSmartphone-only.statsPlayerName");

        List<PlayerInfo> playerInfoList = new ArrayList<>();
        for (int i = 0; i < playersElements.size(); i++) {
            PlayerInfo playerInfo = new PlayerInfo();

            String[] allNames = playersElements.get(i).text().split("[ ']{2}");


            playerInfo.name = allNames[0];
            playerInfo.nickname = allNames[1];
            playerInfo.surname = allNames[2];

            playerInfoList.add(playerInfo);
        }

        if (first) {
            matchInfo.players1 = playerInfoList;
        } else {
            matchInfo.players2 = playerInfoList;
        }
    }


    private void parseDate(Document doc, MatchInfo matchInfo) throws ParseException {
        Element matchInfoElement = doc.select("div.teamsBox").get(0);
        // добавляем дату
        String stringTime = matchInfoElement.select("div.time").text();
        String stringDate = matchInfoElement.select("div.date").text();
        matchInfo.date = DateConverter.convertToDate(stringDate, stringTime);
    }

    private int parseHLTVPositions(Elements rankPosElement, boolean isFirst) throws DataParsingException {
        int number = isFirst ? 0 : 1;
        String text;
        try {
            text = rankPosElement.get(number).text();
        } catch (IndexOutOfBoundsException e) {
            throw new DataParsingException("Участники матча не определены");
        }

        String[] strings = text.split(" ");
        if (text.contains("Unranked")) return -1;

        char[] positionCharArray = strings[2].toCharArray();

        positionCharArray = Arrays.copyOfRange(positionCharArray, 1, positionCharArray.length);

        return Integer.parseInt(new String(positionCharArray));
    }
}

class DataParsingException extends Exception {
    public DataParsingException(String message){
        super(message);
    }
}
