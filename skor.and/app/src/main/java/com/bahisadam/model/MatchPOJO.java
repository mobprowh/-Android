package com.bahisadam.model;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "isSuccess",
        "errorType",
        "data",
        "error"

})
public class MatchPOJO {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    @JsonProperty("errorType")
    private String error;
    @JsonProperty("error")
    private String errorType;
    @JsonProperty("data")
    private Data data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The isSuccess
     */
    @JsonProperty("isSuccess")
    public Boolean getIsSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess The isSuccess
     */
    @JsonProperty("isSuccess")
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * @return The errorType
     */
    @JsonProperty("errorType")
    public String getErrorType() {
        return errorType;
    }

    /**
     * @param errorType The errorType
     */
    @JsonProperty("errorType")
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * @return The error
     */
    @JsonProperty("error")
    public String getError() {
        return error;
    }

    /**
     * @param error The errorType
     */
    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return The data
     */
    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "matches",
            "total"
    })
    public class Data {

        @JsonProperty("matches")
        private List<Match> matches = new ArrayList<Match>();
        @JsonProperty("total")
        private Integer total;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The matches
         */
        @JsonProperty("matches")
        public List<Match> getMatches() {
            return matches;
        }

        /**
         * @param matches The matches
         */
        @JsonProperty("matches")
        public void setMatches(List<Match> matches) {
            this.matches = matches;
        }

        /**
         * @return The total
         */
        @JsonProperty("total")
        public Integer getTotal() {
            return total;
        }

        /**
         * @param total The total
         */
        @JsonProperty("total")
        public void setTotal(Integer total) {
            this.total = total;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }



    @Generated("org.jsonschema2pojo")
    public class Analysis {

        /**
         *
         * (Required)
         *
         */
        @SerializedName("away")
        @Expose
        private String away;
        /**
         *
         * (Required)
         *
         */
        @SerializedName("home")
        @Expose
        private String home;

        /**
         *
         * (Required)
         *
         * @return
         * The away
         */
        public String getAway() {
            return away;
        }

        /**
         *
         * (Required)
         *
         * @param away
         * The away
         */
        public void setAway(String away) {
            this.away = away;
        }

        /**
         *
         * (Required)
         *
         * @return
         * The home
         */
        public String getHome() {
            return home;
        }

        /**
         *
         * (Required)
         *
         * @param home
         * The home
         */
        public void setHome(String home) {
            this.home = home;
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "home_team",
            "away_team",
            "league_id",
            "match_date",
            "round",
            "year",
            "result_type",
            "is_half_time",
            "half_time_result",
            "half_time_home_score",
            "half_time_away_score",
            "odds",
            "live_minute",
            "home_goals",
            "away_goals",
            "events",
            "analysis",
            "forecastCount",
            "country"
    })
    public class Match {

        @JsonProperty("_id")
        private String _id;
        @JsonProperty("home_team")
        private HomeTeam home_team;
        @JsonProperty("away_team")
        private AwayTeam away_team;
        @JsonProperty("league_id")
        private LeagueId league_id;
        @JsonProperty("match_date")
        private String match_date;
        @JsonProperty("round")
        private Integer round;
        @JsonProperty("year")
        private Integer year;
        @JsonProperty("result_type")
        private String result_type;
        @JsonProperty("is_half_time")
        private Boolean is_half_time;
        @JsonProperty("half_time_result")
        private String half_time_result;
        @JsonProperty("half_time_home_score")
        private Integer half_time_home_score;
        @JsonProperty("half_time_away_score")
        private Integer half_time_away_score;
        @JsonProperty("odds")
        private Odds odds;
        @JsonProperty("live_minute")
        private Integer live_minute;
        @JsonProperty("home_goals")
        private Integer home_goals;
        @JsonProperty("away_goals")
        private Integer away_goals;
        @JsonProperty("events")
        private List<Event> events = new ArrayList<Event>();
        @JsonProperty("analysis")
        private Analysis analysis;
        @JsonProperty("forecastCount")
        private Integer forecastCount;
        @JsonProperty("country")
        private Country country;
        @JsonProperty("link")
        private String link;
        public String iddaa_code;
        private boolean is_favorite = false;
        public boolean getIsFavorite() {
            return this.is_favorite;
        }

        public void setIsFavorite(boolean value) {
            this.is_favorite = value;
        }

        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        private Integer layout;

        public Integer getLayout() {
            return layout;
        }

        public void setLayout(Integer layout) {
            this.layout = layout;
        }

        /**
         * @return The id
         */
        @JsonProperty("_id")
        public String getId() {
            return _id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("id")
        public void setId(String _id) {
            this._id = _id;
        }

        /**
         * @return The homeTeam
         */
        @JsonProperty("home_team")
        public HomeTeam getHomeTeam() {
            return home_team;
        }

        /**
         * @param homeTeam The home_team
         */
        @JsonProperty("home_team")
        public void setHomeTeam(HomeTeam home_team) {
            this.home_team = home_team;
        }

        /**
         * @return The awayTeam
         */
        @JsonProperty("away_team")
        public AwayTeam getAwayTeam() {
            return away_team;
        }

        /**
         * @param awayTeam The away_team
         */
        @JsonProperty("away_team")
        public void setAwayTeam(AwayTeam away_team) {
            this.away_team = away_team;
        }

        /**
         * @return The leagueId
         */
        @JsonProperty("league_id")
        public LeagueId getLeagueId() {
            return league_id;
        }

        /**
         * @param leagueId The league_id
         */
        @JsonProperty("league_id")
        public void setLeagueId(LeagueId league_id) {
            this.league_id = league_id;
        }

        /**
         * @return The matchDate
         */
        @JsonProperty("match_date")
        public String getMatchDate() {
            return match_date;
        }

        /**
         * @param matchDate The match_date
         */
        @JsonProperty("match_date")
        public void setMatchDate(String match_date) {
            this.match_date = match_date;
        }

        /**
         * @return The round
         */
        @JsonProperty("round")
        public Integer getRound() {
            return round;
        }

        /**
         * @param round The round
         */
        @JsonProperty("round")
        public void setRound(Integer round) {
            this.round = round;
        }

        /**
         * @return The year
         */
        @JsonProperty("year")
        public Integer getYear() {
            return year;
        }

        /**
         * @param year The year
         */
        @JsonProperty("year")
        public void setYear(Integer year) {
            this.year = year;
        }


        /**
         * @return The resultType
         */
        @JsonProperty("result_type")
        public String getResultType() {
            return result_type;
        }

        /**
         * @param resultType The result_type
         */
        @JsonProperty("result_type")
        public void setResultType(String result_type) {
            this.result_type = result_type;
        }

        /**
         * @return The isHalfTime
         */
        @JsonProperty("is_half_time")
        public Boolean getIsHalfTime() {
            return is_half_time;
        }

        /**
         * @param isHalfTime The is_half_time
         */
        @JsonProperty("is_half_time")
        public void setIsHalfTime(Boolean is_half_time) {
            this.is_half_time = is_half_time;
        }

        /**
         * @return The halfTimeResult
         */
        @JsonProperty("half_time_result")
        public String getHalfTimeResult() {
            return half_time_result;
        }

        /**
         * @param halfTimeResult The half_time_result
         */
        @JsonProperty("half_time_result")
        public void setHalfTimeResult(String half_time_result) {
            this.half_time_result = half_time_result;
        }

        /**
         * @return The halfTimeHomeScore
         */
        @JsonProperty("half_time_home_score")
        public Integer getHalfTimeHomeScore() {
            return half_time_home_score;
        }

        /**
         * @param halfTimeHomeScore The half_time_home_score
         */
        @JsonProperty("half_time_home_score")
        public void setHalfTimeHomeScore(Integer half_time_home_score) {
            this.half_time_home_score = half_time_home_score;
        }

        /**
         * @return The halfTimeAwayScore
         */
        @JsonProperty("half_time_away_score")
        public Integer getHalfTimeAwayScore() {
            return half_time_away_score;
        }

        /**
         * @param halfTimeAwayScore The half_time_away_score
         */
        @JsonProperty("half_time_away_score")
        public void setHalfTimeAwayScore(Integer half_time_away_score) {
            this.half_time_away_score = half_time_away_score;
        }

        /**
         * @return The odds
         */
        @JsonProperty("odds")
        public Odds getOdds() {
            return odds;
        }

        /**
         * @param odds The odds
         */
        @JsonProperty("odds")
        public void setOdds(Odds odds) {
            this.odds = odds;
        }

        /**
         * @return The liveMinute
         */
        @JsonProperty("live_minute")
        public Integer getLiveMinute() {
            return live_minute;
        }

        /**
         * @param liveMinute The live_minute
         */
        @JsonProperty("live_minute")
        public void setLiveMinute(Integer live_minute) {
            this.live_minute = live_minute;
        }

        /**
         * @return The homeGoals
         */
        @JsonProperty("home_goals")
        public Integer getHomeGoals() {
            return home_goals;
        }

        /**
         * @param homeGoals The home_goals
         */
        @JsonProperty("home_goals")
        public void setHomeGoals(Integer home_goals) {
            this.home_goals = home_goals;
        }

        /**
         * @return The awayGoals
         */
        @JsonProperty("away_goals")
        public Integer getAwayGoals() {
            return away_goals;
        }

        /**
         * @param awayGoals The away_goals
         */
        @JsonProperty("away_goals")
        public void setAwayGoals(Integer away_goals) {
            this.away_goals = away_goals;
        }

        /**
         * @return The events
         */
        @JsonProperty("events")
        public List<Event> getEvents() {
            return events;
        }

        /**
         * @param events The events
         */
        @JsonProperty("events")
        public void setEvents(List<Event> events) {
            this.events = events;
        }

        /**
         * @return The analysis
         */
        @JsonProperty("analysis")
        public Analysis getAnalysis() {
            return analysis;
        }

        /**
         * @param analysis The analysis
         */
        @JsonProperty("analysis")
        public void setAnalysis(Analysis analysis) {
            this.analysis = analysis;
        }

        /**
         * @return The forecastCount
         */
        @JsonProperty("forecastCount")
        public Integer getForecastCount() {
            return forecastCount;
        }

        /**
         * @param forecastCount The forecastCount
         */
        @JsonProperty("forecastCount")
        public void setForecastCount(Integer forecastCount) {
            this.forecastCount = forecastCount;
        }

        /**
         * @return The country
         */
        @JsonProperty("country")
        public Country getCountry() {
            return country;
        }

        /**
         * @param country The country
         */
        @JsonProperty("country")
        public void setCountry(Country country) {
            this.country = country;
        }
        @JsonProperty("link")
        public String getLink() {
            return link;
        }
        @JsonProperty("link")
        public void setLink(String link) {
            this.link = link;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "event_type",
            "team",
            "player_alias",
            "player_rs_id",
            "player",
            "minute"
    })
    public class Event {

        @SerializedName("event_type")
        @Expose
        private String event_type;
        @JsonProperty("team")
        private Integer team;
        @JsonProperty("player_alias")
        private String player_alias;
        @JsonProperty("player_rs_id")
        private String player_rs_id;
        @JsonProperty("player")
        private String player;
        @JsonProperty("minute")
        private Integer minute;
        @SerializedName("action_type")
        @Expose
        private String actionType;

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The eventType
         */
        @JsonProperty("event_type")
        public String getEventType() {
            return event_type;
        }

        /**
         * @param eventType The event_type
         */
        @JsonProperty("event_type")
        public void setEventType(String event_type) {
            this.event_type = event_type;
        }

        /**
         * @return The team
         */
        @JsonProperty("team")
        public Integer getTeam() {
            return team;
        }

        /**
         * @param team The team
         */
        @JsonProperty("team")
        public void setTeam(Integer team) {
            this.team = team;
        }

        /**
         * @return The playerAlias
         */
        @JsonProperty("player_alias")
        public String getPlayerAlias() {
            return player_alias;
        }

        /**
         * @param playerAlias The player_alias
         */
        @JsonProperty("player_alias")
        public void setPlayerAlias(String player_alias) {
            this.player_alias = player_alias;
        }

        /**
         * @return The playerRsId
         */
        @JsonProperty("player_rs_id")
        public String getPlayerRsId() {
            return player_rs_id;
        }

        /**
         * @param playerRsId The player_rs_id
         */
        @JsonProperty("player_rs_id")
        public void setPlayerRsId(String player_rs_id) {
            this.player_rs_id = player_rs_id;
        }

        /**
         * @return The player
         */
        @JsonProperty("player")
        public String getPlayer() {
            return player;
        }

        /**
         * @param player The player
         */
        @JsonProperty("player")
        public void setPlayer(String player) {
            this.player = player;
        }

        /**
         * @return The minute
         */
        @JsonProperty("minute")
        public Integer getMinute() {
            return minute;
        }

        /**
         * @param minute The minute
         */
        @JsonProperty("minute")
        public void setMinute(Integer minute) {
            this.minute = minute;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }



    public class IddaaExtended {
        @SerializedName("1")
        @Expose
        private Integer _1;
        @SerializedName("2")
        @Expose
        private Integer _2;
        @SerializedName("12")
        @Expose
        private Integer _12;
        @SerializedName("X")
        @Expose
        private Integer x;
        @SerializedName("IY1")
        @Expose
        private Integer iY1;
        @SerializedName("IYX")
        @Expose
        private Integer iYX;
        @SerializedName("IY2")
        @Expose
        private Integer iY2;
        @SerializedName("15\u00dcst")
        @Expose
        private Integer _15St;
        @SerializedName("15Alt")
        @Expose
        private Integer _15Alt;
        @SerializedName("\u00dcst")
        @Expose
        private Integer st;
        @SerializedName("Alt")
        @Expose
        private Integer alt;
        @SerializedName("35\u00dcst")
        @Expose
        private Integer _35St;
        @SerializedName("35Alt")
        @Expose
        private Integer _35Alt;
        @SerializedName("KGV")
        @Expose
        private Integer kGV;
        @SerializedName("KGY")
        @Expose
        private Integer kGY;
        @SerializedName("1X")
        @Expose
        private Integer _1X;
        @SerializedName("X2")
        @Expose
        private Integer x2;
        @SerializedName("GS01")
        @Expose
        private Integer gS01;
        @SerializedName("GS23")
        @Expose
        private Integer gS23;
        @SerializedName("GS46")
        @Expose
        private Integer gS46;
        @SerializedName("GS7P")
        @Expose
        private Integer gS7P;
        @SerializedName("IY15\u00dcst")
        @Expose
        private Integer iY15St;
        @SerializedName("IY15Alt")
        @Expose
        private Integer iY15Alt;


        public Integer get_1() {
            return _1;
        }

        public void set_1(Integer _1) {
            this._1 = _1;
        }

        public Integer get_2() {
            return _2;
        }

        public void set_2(Integer _2) {
            this._2 = _2;
        }

        public Integer get_12() {
            return _12;
        }

        public void set_12(Integer _12) {
            this._12 = _12;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getiY1() {
            return iY1;
        }

        public void setiY1(Integer iY1) {
            this.iY1 = iY1;
        }

        public Integer getiYX() {
            return iYX;
        }

        public void setiYX(Integer iYX) {
            this.iYX = iYX;
        }

        public Integer getiY2() {
            return iY2;
        }

        public void setiY2(Integer iY2) {
            this.iY2 = iY2;
        }

        public Integer get_15St() {
            return _15St;
        }

        public void set_15St(Integer _15St) {
            this._15St = _15St;
        }

        public Integer get_15Alt() {
            return _15Alt;
        }

        public void set_15Alt(Integer _15Alt) {
            this._15Alt = _15Alt;
        }

        public Integer getSt() {
            return st;
        }

        public void setSt(Integer st) {
            this.st = st;
        }

        public Integer getAlt() {
            return alt;
        }

        public void setAlt(Integer alt) {
            this.alt = alt;
        }

        public Integer get_35St() {
            return _35St;
        }

        public void set_35St(Integer _35St) {
            this._35St = _35St;
        }

        public Integer get_35Alt() {
            return _35Alt;
        }

        public void set_35Alt(Integer _35Alt) {
            this._35Alt = _35Alt;
        }

        public Integer getkGV() {
            return kGV;
        }

        public void setkGV(Integer kGV) {
            this.kGV = kGV;
        }

        public Integer getkGY() {
            return kGY;
        }

        public void setkGY(Integer kGY) {
            this.kGY = kGY;
        }

        public Integer get_1X() {
            return _1X;
        }

        public void set_1X(Integer _1X) {
            this._1X = _1X;
        }

        public Integer getX2() {
            return x2;
        }

        public void setX2(Integer x2) {
            this.x2 = x2;
        }

        public Integer getgS01() {
            return gS01;
        }

        public void setgS01(Integer gS01) {
            this.gS01 = gS01;
        }

        public Integer getgS23() {
            return gS23;
        }

        public void setgS23(Integer gS23) {
            this.gS23 = gS23;
        }

        public Integer getgS46() {
            return gS46;
        }

        public void setgS46(Integer gS46) {
            this.gS46 = gS46;
        }

        public Integer getgS7P() {
            return gS7P;
        }

        public void setgS7P(Integer gS7P) {
            this.gS7P = gS7P;
        }

        public Integer getiY15St() {
            return iY15St;
        }

        public void setiY15St(Integer iY15St) {
            this.iY15St = iY15St;
        }

        public Integer getiY15Alt() {
            return iY15Alt;
        }

        public void setiY15Alt(Integer iY15Alt) {
            this.iY15Alt = iY15Alt;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "team_name",
            "team_name_tr",
            "resultados_id",
            "short_name",
            "stadium",
            "color1",
            "color2",
    //        "team_logos",
            "seo_name"
    })
    public class HomeTeam {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("team_name")
        private String team_name;
        @JsonProperty("team_name_tr")
        private String team_name_tr;
        @JsonProperty("resultados_id")
        private Integer resultados_id;
        @JsonProperty("short_name")
        private String short_name;
        @JsonProperty("stadium")
        private Stadium stadium;
        @JsonProperty("color1")
        private String color1;
        @JsonProperty("color2")
        private String color2;
        private List<Match> allMatches;
        private List<Match> homeMatches;
        private List<Match> awayMatches;

        public List<Match> getHomeMatches() {
            return homeMatches;
        }

        public void setHomeMatches(List<Match> homeMatches) {
            this.homeMatches = homeMatches;
        }

        public List<Match> getAwayMatches() {
            return awayMatches;
        }

        public void setAwayMatches(List<Match> awayMatches) {
            this.awayMatches = awayMatches;
        }

        //   @JsonProperty("team_logos")
        @SerializedName("team_logos1")
        @Expose
        private List<String> team_logos = new ArrayList<String>();
        @JsonProperty("seo_name")
        private String seo_name;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public List<String> getTeamLogos() {
            return team_logos;
        }

        public void setTeamLogos(List<String> team_logos) {
            this.team_logos = team_logos;
        }

        /**
         * @return The id
         */
        @JsonProperty("id")
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("id")
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return The teamName
         */
        @JsonProperty("team_name")
        public String getTeamName() {
            return team_name;
        }

        /**
         * @param teamName The team_name
         */
        @JsonProperty("team_name")
        public void setTeamName(String team_name) {
            this.team_name = team_name;
        }

        /**
         * @return The teamNameTr
         */
        @JsonProperty("team_name_tr")
        public String getTeamNameTr() {
            return team_name_tr;
        }

        /**
         * @param teamNameTr The team_name_tr
         */
        @JsonProperty("team_name_tr")
        public void setTeamNameTr(String team_name_tr) {
            this.team_name_tr = team_name_tr;
        }
        public List<Match> getAllMatches() {
            return allMatches;
        }

        public void setAllMatches(List<Match> allMatches) {
            this.allMatches = allMatches;
        }
        /**
         * @return The resultadosId
         */
        @JsonProperty("resultados_id")
        public Integer getResultadosId() {
            return resultados_id;
        }

        /**
         * @param resultadosId The resultados_id
         */
        @JsonProperty("resultados_id")
        public void setResultadosId(Integer resultados_id) {
            this.resultados_id = resultados_id;
        }

        /**
         * @return The shortName
         */
        @JsonProperty("short_name")
        public String getShortName() {
            return short_name;
        }

        /**
         * @param shortName The short_name
         */
        @JsonProperty("short_name")
        public void setShortName(String short_name) {
            this.short_name = short_name;
        }

        /**
         * @return The stadium
         */
        @JsonProperty("stadium")
        public Stadium getStadium() {
            return stadium;
        }

        /**
         * @param stadium The stadium
         */
        @JsonProperty("stadium")
        public void setStadium(Stadium stadium) {
            this.stadium = stadium;
        }

        /**
         * @return The color1
         */
        @JsonProperty("color1")
        public String getColor1() {
            return color1;
        }

        /**
         * @param color1 The color1
         */
        @JsonProperty("color1")
        public void setColor1(String color1) {
            this.color1 = color1;
        }

        /**
         * @return The color2
         */
        @JsonProperty("color2")
        public String getColor2() {
            return color2;
        }

        /**
         * @param color2 The color2
         */
        @JsonProperty("color2")
        public void setColor2(String color2) {
            this.color2 = color2;
        }



        /**
         * @return The seoName
         */
        @JsonProperty("seo_name")
        public String getSeoName() {
            return seo_name;
        }

        /**
         * @param seoName The seo_name
         */
        @JsonProperty("seo_name")
        public void setSeoName(String seo_name) {
            this.seo_name = seo_name;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "1",
            "2",
            "12",
            "han",
            "X",
            "1X",
            "X2"
    })
    @Generated("org.jsonschema2pojo")
    public class Iddaa {


        @SerializedName("1")
        @Expose
        private Double _1;
        @SerializedName("2")
        @Expose
        private double _2;
        @SerializedName("12")
        @Expose
        private Double _12;
        @SerializedName("han")
        @Expose
        private Double han;

        @SerializedName("han-a")
        @Expose
        private Double hanA;

        @SerializedName("IY15Alt")
        @Expose
        private Double iY15Alt;
        @SerializedName("IY15\u00dcst")
        @Expose
        private Double iY15St;
        @SerializedName("15Alt")
        @Expose
        private Double _15Alt;
        @SerializedName("15\u00dcst")
        @Expose
        private Double _15St;
        @SerializedName("Alt")
        @Expose
        private Double alt;
        @SerializedName("\u00dcst")
        @Expose
        private Double st;
        @SerializedName("35Alt")
        @Expose
        private Double _35Alt;
        @SerializedName("35\u00dcst")
        @Expose
        private Double _35St;
        @SerializedName("IY1")
        @Expose
        private Double iY1;
        @SerializedName("IYX")
        @Expose
        private Double iYX;
        @SerializedName("IY2")
        @Expose
        private Double iY2;
        @SerializedName("1X")
        @Expose
        private Double _1X;
        @SerializedName("X2")
        @Expose
        private Double x2;
        @SerializedName("H1")
        @Expose
        private Double h1;
        @SerializedName("HX")
        @Expose
        private Double hX;
        @SerializedName("H2")
        @Expose
        private Double h2;
        @SerializedName("SF11")
        @Expose
        private Double sF11;
        @SerializedName("SF1X")
        @Expose
        private Double sF1X;
        @SerializedName("SF12")
        @Expose
        private Double sF12;
        @SerializedName("SFX1")
        @Expose
        private Double sFX1;
        @SerializedName("SFXX")
        @Expose
        private Double sFXX;
        @SerializedName("SFX2")
        @Expose
        private Double sFX2;
        @SerializedName("SF21")
        @Expose
        private Double sF21;
        @SerializedName("SF2X")
        @Expose
        private Double sF2X;
        @SerializedName("SF22")
        @Expose
        private Double sF22;
        @SerializedName("KGV")
        @Expose
        private Double kGV;
        @SerializedName("KGY")
        @Expose
        private Double kGY;
        @SerializedName("GS01")
        @Expose
        private Double gS01;
        @SerializedName("GS23")
        @Expose
        private Double gS23;
        @SerializedName("GS46")
        @Expose
        private Double gS46;
        @SerializedName("GS7P")
        @Expose
        private Double gS7P;

        public Double getHanA() {
            return hanA;
        }

        public void setHanA(Double hanA) {
            this.hanA = hanA;
        }

        public Double getiY15Alt() {
            return iY15Alt;
        }

        public void setiY15Alt(Double iY15Alt) {
            this.iY15Alt = iY15Alt;
        }

        public Double getiY15St() {
            return iY15St;
        }

        public void setiY15St(Double iY15St) {
            this.iY15St = iY15St;
        }

        public Double get_15Alt() {
            return _15Alt;
        }

        public void set_15Alt(Double _15Alt) {
            this._15Alt = _15Alt;
        }

        public Double get_15St() {
            return _15St;
        }

        public void set_15St(Double _15St) {
            this._15St = _15St;
        }

        public Double getAlt() {
            return alt;
        }

        public void setAlt(Double alt) {
            this.alt = alt;
        }

        public Double getSt() {
            return st;
        }

        public void setSt(Double st) {
            this.st = st;
        }

        public Double get_35Alt() {
            return _35Alt;
        }

        public void set_35Alt(Double _35Alt) {
            this._35Alt = _35Alt;
        }

        public Double get_35St() {
            return _35St;
        }

        public void set_35St(Double _35St) {
            this._35St = _35St;
        }

        public Double getiY1() {
            return iY1;
        }

        public void setiY1(Double iY1) {
            this.iY1 = iY1;
        }

        public Double getiYX() {
            return iYX;
        }

        public void setiYX(Double iYX) {
            this.iYX = iYX;
        }

        public Double getiY2() {
            return iY2;
        }

        public void setiY2(Double iY2) {
            this.iY2 = iY2;
        }

        public Double get_1X() {
            return _1X;
        }

        public void set_1X(Double _1X) {
            this._1X = _1X;
        }

        public Double getH1() {
            return h1;
        }

        public void setH1(Double h1) {
            this.h1 = h1;
        }

        public Double gethX() {
            return hX;
        }

        public void sethX(Double hX) {
            this.hX = hX;
        }

        public Double getH2() {
            return h2;
        }

        public void setH2(Double h2) {
            this.h2 = h2;
        }

        public Double getsF11() {
            return sF11;
        }

        public void setsF11(Double sF11) {
            this.sF11 = sF11;
        }

        public Double getsF1X() {
            return sF1X;
        }

        public void setsF1X(Double sF1X) {
            this.sF1X = sF1X;
        }

        public Double getsF12() {
            return sF12;
        }

        public void setsF12(Double sF12) {
            this.sF12 = sF12;
        }

        public Double getsFX1() {
            return sFX1;
        }

        public void setsFX1(Double sFX1) {
            this.sFX1 = sFX1;
        }

        public Double getsFXX() {
            return sFXX;
        }

        public void setsFXX(Double sFXX) {
            this.sFXX = sFXX;
        }

        public Double getsFX2() {
            return sFX2;
        }

        public void setsFX2(Double sFX2) {
            this.sFX2 = sFX2;
        }

        public Double getsF21() {
            return sF21;
        }

        public void setsF21(Double sF21) {
            this.sF21 = sF21;
        }

        public Double getsF2X() {
            return sF2X;
        }

        public void setsF2X(Double sF2X) {
            this.sF2X = sF2X;
        }

        public Double getsF22() {
            return sF22;
        }

        public void setsF22(Double sF22) {
            this.sF22 = sF22;
        }

        public Double getkGV() {
            return kGV;
        }

        public void setkGV(Double kGV) {
            this.kGV = kGV;
        }

        public Double getkGY() {
            return kGY;
        }

        public void setkGY(Double kGY) {
            this.kGY = kGY;
        }

        public Double getgS01() {
            return gS01;
        }

        public void setgS01(Double gS01) {
            this.gS01 = gS01;
        }

        public Double getgS23() {
            return gS23;
        }

        public void setgS23(Double gS23) {
            this.gS23 = gS23;
        }

        public Double getgS46() {
            return gS46;
        }

        public void setgS46(Double gS46) {
            this.gS46 = gS46;
        }

        public Double getgS7P() {
            return gS7P;
        }

        public void setgS7P(Double gS7P) {
            this.gS7P = gS7P;
        }

        public Double get1() {
            return _1;
        }

        public void set1(Double _1) {
            this._1 = _1;
        }

        public void set2(Double _2){
            this._2 = _2;
        }
        public Double get2(){
            return this._2;
        }

        public Double get12() {
            return _12;
        }

        public void set12(Double one_two) {
            this._12 = one_two;
        }

        public Double getHan() {
            return han;
        }

        public void setHan(Double han) {
            this.han = han;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }



        public Double getX2() {
            return x2;
        }

        public void setX2(Double x2) {
            this.x2 = x2;
        }

        @SerializedName("X")
        @Expose
        private Double x;


        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "league_name",
            "league_name_tr",
            "resultados_id",
            "order",
            "type",
            "teams",
            "sub_league",
            "current_round",
            "group_code",
            "phase",
            "playoff",
            "total_rounds",
            "year",
            "country_id",
            "active"
    })
    public class LeagueId {

        @JsonProperty("_id")
        private Integer _id;
        @JsonProperty("league_name")
        private String league_name;
        @JsonProperty("league_name_tr")
        private String league_name_tr;
        @JsonProperty("resultados_id")
        private Integer resultados_id;
        @JsonProperty("order")
        private Integer order;
        @JsonProperty("teams")
        private List<Integer> teams = new ArrayList<Integer>();
        @JsonProperty("sub_league")
        private Boolean sub_league;
        @JsonProperty("current_round")
        private Integer current_round;
        @JsonProperty("group_code")
        private String group_code;
        @JsonProperty("phase")
        private String phase;
        @JsonProperty("playoff")
        private String playoff;
        @JsonProperty("total_rounds")
        private Integer total_rounds;
        @JsonProperty("year")
        private Integer year;
        @SerializedName("type")
        @Expose
        private String type;
        @JsonProperty("country_id")
        private Integer country_id;
        @JsonProperty("active")
        private Boolean active;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        /**
         * @return The id
         */
        @JsonProperty("_id")
        public Integer getId() {
            return _id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("_id")
        public void setId(Integer _id) {
            this._id = _id;
        }

        /**
         * @return The leagueName
         */
        @JsonProperty("league_name")
        public String getLeagueName() {
            return league_name;
        }

        /**
         * @param leagueName The league_name
         */
        @JsonProperty("league_name")
        public void setLeagueName(String league_name) {
            this.league_name = league_name;
        }

        /**
         * @return The leagueNameTr
         */
        @JsonProperty("league_name_tr")
        public String getLeagueNameTr() {
            return league_name_tr;
        }

        /**
         * @param leagueNameTr The league_name_tr
         */
        @JsonProperty("league_name_tr")
        public void setLeagueNameTr(String league_name_tr) {
            this.league_name_tr = league_name_tr;
        }

        /**
         * @return The resultadosId
         */
        @JsonProperty("resultados_id")
        public Integer getResultadosId() {
            return resultados_id;
        }

        /**
         * @param resultadosId The resultados_id
         */
        @JsonProperty("resultados_id")
        public void setResultadosId(Integer resultados_id) {
            this.resultados_id = resultados_id;
        }

        /**
         * @return The order
         */
        @JsonProperty("order")
        public Integer getOrder() {
            return order;
        }

        /**
         * @param order The order
         */
        @JsonProperty("order")
        public void setOrder(Integer order) {
            this.order = order;
        }

        /**
         * @return The type
         */
        @JsonProperty("type")
        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */
        @JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return The teams
         */
        @JsonProperty("teams")
        public List<Integer> getTeams() {
            return teams;
        }

        /**
         * @param teams The teams
         */
        @JsonProperty("teams")
        public void setTeams(List<Integer> teams) {
            this.teams = teams;
        }

        /**
         * @return The subLeague
         */
        @JsonProperty("sub_league")
        public Boolean getSubLeague() {
            return sub_league;
        }

        /**
         * @param subLeague The sub_league
         */
        @JsonProperty("sub_league")
        public void setSubLeague(Boolean sub_league) {
            this.sub_league = sub_league;
        }

        /**
         * @return The currentRound
         */
        @JsonProperty("current_round")
        public Integer getCurrentRound() {
            return current_round;
        }

        /**
         * @param currentRound The current_round
         */
        @JsonProperty("current_round")
        public void setCurrentRound(Integer current_round) {
            this.current_round = current_round;
        }

        /**
         * @return The groupCode
         */
        @JsonProperty("group_code")
        public String getGroupCode() {
            return group_code;
        }

        /**
         * @param groupCode The group_code
         */
        @JsonProperty("group_code")
        public void setGroupCode(String group_code) {
            this.group_code = group_code;
        }

        /**
         * @return The phase
         */
        @JsonProperty("phase")
        public String getPhase() {
            return phase;
        }

        /**
         * @param phase The phase
         */
        @JsonProperty("phase")
        public void setPhase(String phase) {
            this.phase = phase;
        }

        /**
         * @return The playoff
         */
        @JsonProperty("playoff")
        public String getPlayoff() {
            return playoff;
        }

        /**
         * @param playoff The playoff
         */
        @JsonProperty("playoff")
        public void setPlayoff(String playoff) {
            this.playoff = playoff;
        }

        /**
         * @return The totalRounds
         */
        @JsonProperty("total_rounds")
        public Integer getTotalRounds() {
            return total_rounds;
        }

        /**
         * @param totalRounds The total_rounds
         */
        @JsonProperty("total_rounds")
        public void setTotalRounds(Integer total_rounds) {
            this.total_rounds = total_rounds;
        }

        /**
         * @return The year
         */
        @JsonProperty("year")
        public Integer getYear() {
            return year;
        }

        /**
         * @param year The year
         */
        @JsonProperty("year")
        public void setYear(Integer year) {
            this.year = year;
        }

        /**
         * @return The countryId
         */
        @JsonProperty("country_id")
        public Integer getCountryId() {
            return country_id;
        }

        /**
         * @param countryId The country_id
         */
        @JsonProperty("country_id")
        public void setCountryId(Integer country_id) {
            this.country_id = country_id;
        }

        /**
         * @return The active
         */
        @JsonProperty("active")
        public Boolean getActive() {
            return active;
        }

        /**
         * @param active The active
         */
        @JsonProperty("active")
        public void setActive(Boolean active) {
            this.active = active;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        @Override
        public boolean equals(Object obj) {
            return this.getLeagueName().equals(((LeagueId)obj).getLeagueName());
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = hash * 31 + getLeagueName().hashCode();
            return hash;
        }
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "iddaa"
    })
    public class Odds {

        @JsonProperty("iddaa")
        private Iddaa iddaa;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The iddaa
         */
        @JsonProperty("iddaa")
        public Iddaa getIddaa() {
            return iddaa;
        }

        /**
         * @param iddaa The iddaa
         */
        @JsonProperty("iddaa")
        public void setIddaa(Iddaa iddaa) {
            this.iddaa = iddaa;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "name",
            "image",
            "yearBuilt",
            "seats"
    })
    public class Stadium {

        @JsonProperty("name")
        private String name;
        @JsonProperty("image")
        private String image;
        @JsonProperty("yearBuilt")
        private String yearBuilt;
        @JsonProperty("seats")
        private String seats;
        private String attendance;

        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }

        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The name
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The image
         */
        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        /**
         * @param image The image
         */
        @JsonProperty("image")
        public void setImage(String image) {
            this.image = image;
        }

        /**
         * @return The yearBuilt
         */
        @JsonProperty("yearBuilt")
        public String getYearBuilt() {
            return yearBuilt;
        }

        /**
         * @param yearBuilt The yearBuilt
         */
        @JsonProperty("yearBuilt")
        public void setYearBuilt(String yearBuilt) {
            this.yearBuilt = yearBuilt;
        }

        /**
         * @return The seats
         */
        @JsonProperty("seats")
        public String getSeats() {
            return seats;
        }

        /**
         * @param seats The seats
         */
        @JsonProperty("seats")
        public void setSeats(String seats) {
            this.seats = seats;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "name",
            "image",
            "yearBuilt",
            "seats"
    })
    public class Stadium_ {

        @JsonProperty("name")
        private Object name;
        @JsonProperty("image")
        private Object image;
        @JsonProperty("yearBuilt")
        private Object yearBuilt;
        @JsonProperty("seats")
        private Object seats;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The name
         */
        @JsonProperty("name")
        public Object getName() {
            return name;
        }

        /**
         * @param name The name
         */
        @JsonProperty("name")
        public void setName(Object name) {
            this.name = name;
        }

        /**
         * @return The image
         */
        @JsonProperty("image")
        public Object getImage() {
            return image;
        }

        /**
         * @param image The image
         */
        @JsonProperty("image")
        public void setImage(Object image) {
            this.image = image;
        }

        /**
         * @return The yearBuilt
         */
        @JsonProperty("yearBuilt")
        public Object getYearBuilt() {
            return yearBuilt;
        }

        /**
         * @param yearBuilt The yearBuilt
         */
        @JsonProperty("yearBuilt")
        public void setYearBuilt(Object yearBuilt) {
            this.yearBuilt = yearBuilt;
        }

        /**
         * @return The seats
         */
        @JsonProperty("seats")
        public Object getSeats() {
            return seats;
        }

        /**
         * @param seats The seats
         */
        @JsonProperty("seats")
        public void setSeats(Object seats) {
            this.seats = seats;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }
    public class Stats {

        @SerializedName("pos")
        @Expose
        private Double pos;
        @SerializedName("fou")
        @Expose
        private Double fou;
        @SerializedName("sot")
        @Expose
        private Double sot;
        @SerializedName("son")
        @Expose
        private Double son;
        @SerializedName("soff")
        @Expose
        private Double soff;
        @SerializedName("blk")
        @Expose
        private Double blk;
        @SerializedName("frk")
        @Expose
        private Double frk;
        @SerializedName("cor")
        @Expose
        private Double cor;
        @SerializedName("off")
        @Expose
        private Double off;
        @SerializedName("tho")
        @Expose
        private Double tho;
        @SerializedName("yc")
        @Expose
        private Double yc;
        @SerializedName("rc")
        @Expose
        private Double rc;

        /**
         *
         * @return
         * The pos
         */
        public Double getPos() {
            return pos;
        }

        /**
         *
         * @param pos
         * The pos
         */
        public void setPos(Double pos) {
            this.pos = pos;
        }

        /**
         *
         * @return
         * The fou
         */
        public Double getFou() {
            return fou;
        }

        /**
         *
         * @param fou
         * The fou
         */
        public void setFou(Double fou) {
            this.fou = fou;
        }

        /**
         *
         * @return
         * The sot
         */
        public Double getSot() {
            return sot;
        }

        /**
         *
         * @param sot
         * The sot
         */
        public void setSot(Double sot) {
            this.sot = sot;
        }

        /**
         *
         * @return
         * The son
         */
        public Double getSon() {
            return son;
        }

        /**
         *
         * @param son
         * The son
         */
        public void setSon(Double son) {
            this.son = son;
        }

        /**
         *
         * @return
         * The soff
         */
        public Double getSoff() {
            return soff;
        }

        /**
         *
         * @param soff
         * The soff
         */
        public void setSoff(Double soff) {
            this.soff = soff;
        }

        /**
         *
         * @return
         * The blk
         */
        public Double getBlk() {
            return blk;
        }

        /**
         *
         * @param blk
         * The blk
         */
        public void setBlk(Double blk) {
            this.blk = blk;
        }

        /**
         *
         * @return
         * The frk
         */
        public Double getFrk() {
            return frk;
        }

        /**
         *
         * @param frk
         * The frk
         */
        public void setFrk(Double frk) {
            this.frk = frk;
        }

        /**
         *
         * @return
         * The cor
         */
        public Double getCor() {
            return cor;
        }

        /**
         *
         * @param cor
         * The cor
         */
        public void setCor(Double cor) {
            this.cor = cor;
        }

        /**
         *
         * @return
         * The off
         */
        public Double getOff() {
            return off;
        }

        /**
         *
         * @param off
         * The off
         */
        public void setOff(Double off) {
            this.off = off;
        }

        /**
         *
         * @return
         * The tho
         */
        public Double getTho() {
            return tho;
        }

        /**
         *
         * @param tho
         * The tho
         */
        public void setTho(Double tho) {
            this.tho = tho;
        }

        /**
         *
         * @return
         * The yc
         */
        public Double getYc() {
            return yc;
        }

        /**
         *
         * @param yc
         * The yc
         */
        public void setYc(Double yc) {
            this.yc = yc;
        }

        /**
         *
         * @return
         * The rc
         */
        public Double getRc() {
            return rc;
        }

        /**
         *
         * @param rc
         * The rc
         */
        public void setRc(Double rc) {
            this.rc = rc;
        }

    }

    public class Player{
        public  Integer jersey_number;
        public String type;
        public String name;
        public String id;
    }

    public class Lineups{
        public List<Player> local;
        public List<Player> local_substitutes;
        public List<Player> visitor;
        public List<Player> visitor_substitutes;
    }
    public class MatchDetailed {
        private String id;
        private HomeTeam home_team;
        private AwayTeam away_team;
        private LeagueId league_id;
        private String resultados_id;
        private String match_date;
        private Integer round;
        private Integer year;
        private String result_type;
        private String group_code;
        private Boolean playoffs;
        private Integer attendee;
        private Integer penaltis1;
        private Integer penaltis2;
        private Boolean is_half_time;
        private String half_time_result;
        private Integer half_time_home_score;
        private Integer half_time_away_score;
        private String updated_date;
        private Odds odds;
        private Stadium stadium;
        private String referee;
        private Integer live_minute;
        private Integer home_goals;
        private Integer away_goals;
        private String inserted_date;
        private List<Event> events;
        private List<Stats> stats;
        private List<Comment> comments;
        private Analysis analysis;
        private Forecast forecast;
        private Integer forecastCount;
        private Boolean hasLineup;
        @SerializedName("lineups")
        @Expose
        public Lineups lineups;
        private String result;
        private Iddaa iddaa_odds;
        //private match_companies
        private String country;
        //private List<Match> headtohead;
        private Stats homeAverageStats;
        private Stats awayAverageStats;
        @SerializedName("headtohead")
        @Expose
        private List<Match> headToHead;

        public List<Match> getHeadToHead() {
            return headToHead;
        }

        public void setHeadToHead(List<Match> headToHead) {
            this.headToHead = headToHead;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public HomeTeam getHomeTeam() {
            return home_team;
        }

        public void setHomeTeam(HomeTeam home_team) {
            this.home_team = home_team;
        }

        public AwayTeam getAwayTeam() {
            return away_team;
        }

        public void setAwayTeam(AwayTeam away_team) {
            this.away_team = away_team;
        }

        public LeagueId getLeagueId() {
            return league_id;
        }

        public void setLeagueId(LeagueId league_id) {
            this.league_id = league_id;
        }

        public String getResultadosId() {
            return resultados_id;
        }

        public void setResultadosId(String resultados_id) {
            this.resultados_id = resultados_id;
        }

        public String getMatch_date() {
            return match_date;
        }

        public void setMatch_date(String match_date) {
            this.match_date = match_date;
        }

        public Integer getRound() {
            return round;
        }

        public void setRound(Integer round) {
            this.round = round;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getResult_type() {
            return result_type;
        }

        public void setResult_type(String result_type) {
            this.result_type = result_type;
        }

        public String getGroup_code() {
            return group_code;
        }

        public void setGroup_code(String group_code) {
            this.group_code = group_code;
        }

        public Boolean getPlayoffs() {
            return playoffs;
        }

        public void setPlayoffs(Boolean playoffs) {
            this.playoffs = playoffs;
        }

        public Integer getAttendee() {
            return attendee;
        }

        public void setAttendee(Integer attendee) {
            this.attendee = attendee;
        }

        public Integer getPenaltis1() {
            return penaltis1;
        }

        public void setPenaltis1(Integer penaltis1) {
            this.penaltis1 = penaltis1;
        }

        public Integer getPenaltis2() {
            return penaltis2;
        }

        public void setPenaltis2(Integer penaltis2) {
            this.penaltis2 = penaltis2;
        }

        public Boolean getIs_half_time() {
            return is_half_time;
        }

        public void setIs_half_time(Boolean is_half_time) {
            this.is_half_time = is_half_time;
        }

        public String getHalf_time_result() {
            return half_time_result;
        }

        public void setHalf_time_result(String half_time_result) {
            this.half_time_result = half_time_result;
        }

        public Integer getHalf_time_home_score() {
            return  half_time_home_score == null ? 0 : half_time_home_score;
        }

        public void setHalf_time_home_score(Integer half_time_home_score) {
            this.half_time_home_score = half_time_home_score;
        }

        public Integer getHalf_time_away_score() {
            return half_time_away_score == null ?  0 : half_time_away_score;
        }

        public void setHalf_time_away_score(Integer half_time_away_score) {
            this.half_time_away_score = half_time_away_score;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public Odds getOdds() {
            return odds;
        }

        public void setOdds(Odds odds) {
            this.odds = odds;
        }

        public Stadium getStadium() {
            return stadium;
        }

        public void setStadium(Stadium stadium) {
            this.stadium = stadium;
        }

        public String getReferee() {
            return referee;
        }

        public void setReferee(String referee) {
            this.referee = referee;
        }

        public Integer getLive_minute() {
            return live_minute;
        }

        public void setLive_minute(Integer live_minute) {
            this.live_minute = live_minute;
        }

        public Integer getHome_goals() {
            return home_goals;
        }

        public void setHome_goals(Integer home_goals) {
            this.home_goals = home_goals;
        }

        public Integer getAway_goals() {
            return away_goals;
        }

        public void setAway_goals(Integer away_goals) {
            this.away_goals = away_goals;
        }

        public String getInserted_date() {
            return inserted_date;
        }

        public void setInserted_date(String inserted_date) {
            this.inserted_date = inserted_date;
        }

        public List<Event> getEvents() {
            return events;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
        }

        public List<Stats> getStats() {
            return stats;
        }

        public void setStats(List<Stats> stats) {
            this.stats = stats;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        public Analysis getAnalysis() {
            return analysis;
        }

        public void setAnalysis(Analysis analysis) {
            this.analysis = analysis;
        }

        public Forecast getForecast() {
            return forecast;
        }

        public void setForecast(Forecast forecast) {
            this.forecast = forecast;
        }

        public Integer getForecastCount() {
            return forecastCount;
        }

        public void setForecastCount(Integer forecastCount) {
            this.forecastCount = forecastCount;
        }

        public Boolean getHasLineup() {
            return hasLineup;
        }

        public void setHasLineup(Boolean hasLineup) {
            this.hasLineup = hasLineup;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Iddaa getIddaa_odds() {
            return iddaa_odds;
        }

        public void setIddaa_odds(Iddaa iddaa_odds) {
            this.iddaa_odds = iddaa_odds;
        }

        public String  getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

      /*  public List<Match> getHeadtohead() {
            return headtohead;
        }

        public void setHeadtohead(List<Match> headtohead) {
            this.headtohead = headtohead;
        }
*/
        public Stats getHomeAverageStats() {
            return homeAverageStats;
        }

        public void setHomeAverageStats(Stats homeAverageStats) {
            this.homeAverageStats = homeAverageStats;
        }

        public Stats getAwayAverageStats() {
            return awayAverageStats;
        }

        public void setAwayAverageStats(Stats awayAverageStats) {
            this.awayAverageStats = awayAverageStats;
        }
    }
    @Generated("org.jsonschema2pojo")
    public class Forecast {

        @SerializedName("away")
        @Expose
        private Integer away;
        @SerializedName("draw")
        @Expose
        private Integer draw;
        @SerializedName("home")
        @Expose
        private Integer home;

        /**
         *
         * @return
         * The away
         */
        public Integer getAway() {
            return away;
        }

        /**
         *
         * @param away
         * The away
         */
        public void setAway(Integer away) {
            this.away = away;
        }

        /**
         *
         * @return
         * The draw
         */
        public Integer getDraw() {
            return draw;
        }

        /**
         *
         * @param draw
         * The draw
         */
        public void setDraw(Integer draw) {
            this.draw = draw;
        }

        /**
         *
         * @return
         * The home
         */
        public Integer getHome() {
            return home;
        }

        /**
         *
         * @param home
         * The home
         */
        public void setHome(Integer home) {
            this.home = home;
        }

    }


    public class Logo{
        @SerializedName("0")
        @Expose
        String logo;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "team_name",
            "team_name_tr",
            "resultados_id",
            "stadium",
            "color1",
            "color2",
  //          "team_logos",
            "seo_name"
    })
    public class AwayTeam {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("team_name")
        private String team_name;
        @JsonProperty("team_name_tr")
        private String team_name_tr;
        @JsonProperty("resultados_id")
        private Integer resultados_id;
        @JsonProperty("stadium")
        private Stadium_ stadium;
        @JsonProperty("color1")
        private String color1;
        @JsonProperty("color2")
        private String color2;
        @SerializedName("team_logos1")
        @Expose
        private List<String> team_logos = new ArrayList<String>();
        @JsonProperty("seo_name")
        private String seo_name;
        private List<Match> allMatches;

        private List<Match> homeMatches;
        private List<Match> awayMatches;


        public List<Match> getHomeMatches() {
            return homeMatches;
        }

        public void setHomeMatches(List<Match> homeMatches) {
            this.homeMatches = homeMatches;
        }

        public List<Match> getAwayMatches() {
            return awayMatches;
        }

        public void setAwayMatches(List<Match> awayMatches) {
            this.awayMatches = awayMatches;
        }

        public List<Match> getAllMatches() {
            return allMatches;
        }

        public void setAllMatches(List<Match> allMatches) {
            this.allMatches = allMatches;
        }
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        public List<String> getTeamLogos() {
            return team_logos;
        }

        public void setTeamLogos(List<String> team_logos) {
            this.team_logos = team_logos;
        }

        /**
         * @return The id
         */
        @JsonProperty("id")
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("id")
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return The teamName
         */
        @JsonProperty("team_name")
        public String getTeamName() {
            return team_name;
        }

        /**
         * @param teamName The team_name
         */
        @JsonProperty("team_name")
        public void setTeamName(String team_name) {
            this.team_name = team_name;
        }

        /**
         * @return The teamNameTr
         */
        @JsonProperty("team_name_tr")
        public String getTeamNameTr() {
            return team_name_tr;
        }

        /**
         * @param teamNameTr The team_name_tr
         */
        @JsonProperty("team_name_tr")
        public void setTeamNameTr(String team_name_tr) {
            this.team_name_tr = team_name_tr;
        }

        /**
         * @return The resultadosId
         */
        @JsonProperty("resultados_id")
        public Integer getResultadosId() {
            return resultados_id;
        }

        /**
         * @param resultadosId The resultados_id
         */
        @JsonProperty("resultados_id")
        public void setResultadosId(Integer resultados_id) {
            this.resultados_id = resultados_id;
        }

        /**
         * @return The stadium
         */
        @JsonProperty("stadium")
        public Stadium_ getStadium() {
            return stadium;
        }

        /**
         * @param stadium The stadium
         */
        @JsonProperty("stadium")
        public void setStadium(Stadium_ stadium) {
            this.stadium = stadium;
        }

        /**
         * @return The color1
         */
        @JsonProperty("color1")
        public String getColor1() {
            return color1;
        }

        /**
         * @param color1 The color1
         */
        @JsonProperty("color1")
        public void setColor1(String color1) {
            this.color1 = color1;
        }

        /**
         * @return The color2
         */
        @JsonProperty("color2")
        public String getColor2() {
            return color2;
        }

        /**
         * @param color2 The color2
         */
        @JsonProperty("color2")
        public void setColor2(String color2) {
            this.color2 = color2;
        }

        /**
         * @return The teamLogos
         */


        /**
         * @param teamLogos The team_logos
         */

        /**
         * @return The seoName
         */
        @JsonProperty("seo_name")
        public String getSeoName() {
            return seo_name;
        }

        /**
         * @param seoName The seo_name
         */
        @JsonProperty("seo_name")
        public void setSeoName(String seo_name) {
            this.seo_name = seo_name;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "country_name_tr",
            "country_name",
            "country_code",
            "order",
            "seo_name"
    })
    public class Country {

        @JsonProperty("id")
        private String id;
        @JsonProperty("country_name_tr")
        private String country_name_tr;
        @JsonProperty("country_name")
        private String country_name;
        @JsonProperty("country_code")
        private String country_code;
        @JsonProperty("order")
        private Integer order;
        @JsonProperty("seo_name")
        private String seo_name;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The id
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The countryNameTr
         */
        @JsonProperty("country_name_tr")
        public String getCountryNameTr() {
            return country_name_tr;
        }

        /**
         * @param countryNameTr The country_name_tr
         */
        @JsonProperty("country_name_tr")
        public void setCountryNameTr(String country_name_tr) {
            this.country_name_tr = country_name_tr;
        }

        /**
         * @return The countryName
         */
        @JsonProperty("country_name")
        public String getCountryName() {
            return country_name;
        }

        /**
         * @param countryName The country_name
         */
        @JsonProperty("country_name")
        public void setCountryName(String country_name) {
            this.country_name = country_name;
        }

        /**
         * @return The countryCode
         */
        @JsonProperty("country_code")
        public String getCountryCode() {
            return country_code;
        }

        /**
         * @param countryCode The country_code
         */
        @JsonProperty("country_code")
        public void setCountryCode(String country_code) {
            this.country_code = country_code;
        }

        /**
         * @return The order
         */
        @JsonProperty("order")
        public Integer getOrder() {
            return order;
        }

        /**
         * @param order The order
         */
        @JsonProperty("order")
        public void setOrder(Integer order) {
            this.order = order;
        }

        /**
         * @return The seoName
         */
        @JsonProperty("seo_name")
        public String getSeoName() {
            return seo_name;
        }

        /**
         * @param seoName The seo_name
         */
        @JsonProperty("seo_name")
        public void setSeoName(String seo_name) {
            this.seo_name = seo_name;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }
    @Generated("org.jsonschema2pojo")
    public class Id {

        @SerializedName("team")
        @Expose
        private Integer team;
        @SerializedName("league")
        @Expose
        private Integer league;
        @SerializedName("group_code")
        @Expose
        private String groupCode;
        @SerializedName("year")
        @Expose
        private Integer year;

        /**
         *
         * @return
         * The team
         */
        public Integer getTeam() {
            return team;
        }

        /**
         *
         * @param team
         * The team
         */
        public void setTeam(Integer team) {
            this.team = team;
        }

        /**
         *
         * @return
         * The league
         */
        public Integer getLeague() {
            return league;
        }

        /**
         *
         * @param league
         * The league
         */
        public void setLeague(Integer league) {
            this.league = league;
        }

        /**
         *
         * @return
         * The groupCode
         */
        public String getGroupCode() {
            return groupCode;
        }

        /**
         *
         * @param groupCode
         * The group_code
         */
        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        /**
         *
         * @return
         * The year
         */
        public Integer getYear() {
            return year;
        }

        /**
         *
         * @param year
         * The year
         */
        public void setYear(Integer year) {
            this.year = year;
        }

    }

    @Generated("org.jsonschema2pojo")
    public class Standing {


        @SerializedName("num")
        @Expose
        private Integer num;
        @SerializedName("_id")
        @Expose
        private Id id;
        @SerializedName("OM")
        @Expose
        private Integer oM;
        @SerializedName("G")
        @Expose
        private Integer g;
        @SerializedName("B")
        @Expose
        private Integer b;
        @SerializedName("M")
        @Expose
        private Integer m;
        @SerializedName("AG")
        @Expose
        private Integer aG;
        @SerializedName("YG")
        @Expose
        private Integer yG;
        @SerializedName("AVG")
        @Expose
        private Integer aVG;
        @SerializedName("PTS")
        @Expose
        private Integer pTS;
        @SerializedName("team_name")
        @Expose
        private String teamName;
        @SerializedName("team_name_tr")
        @Expose
        private String teamNameTr;
        @SerializedName("seo_name")
        @Expose
        private String seoName;
        @SerializedName("color1")
        @Expose
        private String color1;
        @SerializedName("color2")
        @Expose
        private String color2;
        @SerializedName("team_logo")
        @Expose
        private String teamLogo;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
        /**
         *
         * @return
         * The id
         */
        public Id getId() {
            return id;
        }

        /**
         *
         * @param id
         * The _id
         */
        public void setId(Id id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The oM
         */
        public Integer getOM() {
            return oM;
        }

        /**
         *
         * @param oM
         * The OM
         */
        public void setOM(Integer oM) {
            this.oM = oM;
        }

        /**
         *
         * @return
         * The g
         */
        public Integer getG() {
            return g;
        }

        /**
         *
         * @param g
         * The G
         */
        public void setG(Integer g) {
            this.g = g;
        }

        /**
         *
         * @return
         * The b
         */
        public Integer getB() {
            return b;
        }

        /**
         *
         * @param b
         * The B
         */
        public void setB(Integer b) {
            this.b = b;
        }

        /**
         *
         * @return
         * The m
         */
        public Integer getM() {
            return m;
        }

        /**
         *
         * @param m
         * The M
         */
        public void setM(Integer m) {
            this.m = m;
        }

        /**
         *
         * @return
         * The aG
         */
        public Integer getAG() {
            return aG;
        }

        /**
         *
         * @param aG
         * The AG
         */
        public void setAG(Integer aG) {
            this.aG = aG;
        }

        /**
         *
         * @return
         * The yG
         */
        public Integer getYG() {
            return yG;
        }

        /**
         *
         * @param yG
         * The YG
         */
        public void setYG(Integer yG) {
            this.yG = yG;
        }

        /**
         *
         * @return
         * The aVG
         */
        public Integer getAVG() {
            return aVG;
        }

        /**
         *
         * @param aVG
         * The AVG
         */
        public void setAVG(Integer aVG) {
            this.aVG = aVG;
        }

        /**
         *
         * @return
         * The pTS
         */
        public Integer getPTS() {
            return pTS;
        }

        /**
         *
         * @param pTS
         * The PTS
         */
        public void setPTS(Integer pTS) {
            this.pTS = pTS;
        }

        /**
         *
         * @return
         * The teamName
         */
        public String getTeamName() {
            return teamName;
        }

        /**
         *
         * @param teamName
         * The team_name
         */
        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        /**
         *
         * @return
         * The teamNameTr
         */
        public String getTeamNameTr() {
            return teamNameTr;
        }

        /**
         *
         * @param teamNameTr
         * The team_name_tr
         */
        public void setTeamNameTr(String teamNameTr) {
            this.teamNameTr = teamNameTr;
        }

        /**
         *
         * @return
         * The seoName
         */
        public String getSeoName() {
            return seoName;
        }

        /**
         *
         * @param seoName
         * The seo_name
         */
        public void setSeoName(String seoName) {
            this.seoName = seoName;
        }

        /**
         *
         * @return
         * The color1
         */
        public String getColor1() {
            return color1;
        }

        /**
         *
         * @param color1
         * The color1
         */
        public void setColor1(String color1) {
            this.color1 = color1;
        }

        /**
         *
         * @return
         * The color2
         */
        public String getColor2() {
            return color2;
        }

        /**
         *
         * @param color2
         * The color2
         */
        public void setColor2(String color2) {
            this.color2 = color2;
        }

        /**
         *
         * @return
         * The teamLogo
         */
        public String getTeamLogo() {
            return teamLogo;
        }

        /**
         *
         * @param teamLogo
         * The team_logo
         */
        public void setTeamLogo(String teamLogo) {
            this.teamLogo = teamLogo;
        }

    }
    public class StandingsRequest{
        private Standings standings;

        public Standings getStandings() {
            return standings;
        }

        public void setStandings(Standings standings) {
            this.standings = standings;
        }


    }
    public class HomeAwayRequest{
        private Standings home;

        public Standings getAway() {
            return away;
        }

        public void setAway(Standings away) {
            this.away = away;
        }

        public Standings getHome() {
            return home;
        }

        public void setHome(Standings home) {
            this.home = home;
        }

        private Standings away;
    }

    public class Standings {

        @SerializedName("1")
        @Expose
        private List<Standing> _1;
        @SerializedName("2")
        @Expose
        private List<Standing> _2;
        @SerializedName("3")
        @Expose
        private List<Standing> _3;
        @SerializedName("4")
        @Expose
        private List<Standing> _4;
        @SerializedName("5")
        @Expose
        private List<Standing> _5;
        @SerializedName("6")
        @Expose
        private List<Standing> _6;
        @SerializedName("7")
        @Expose
        private List<Standing> _7;
        @SerializedName("8")
        @Expose
        private List<Standing> _8;
        @SerializedName("9")
        @Expose
        private List<Standing> _9;

        public List<Standing> get1() {
            return _1;
        }

        public void set1(List<Standing> _1) {
            this._1 = _1;
        }

        public List<Standing> get2() {
            return _2;
        }

        public void set2(List<Standing> _2) {
            this._2 = _2;
        }

        public List<Standing> get3() {
            return _3;
        }

        public void set3(List<Standing> _3) {
            this._3 = _3;
        }

        public List<Standing> get4() {
            return _4;
        }

        public void set4(List<Standing> _4) {
            this._4 = _4;
        }

        public List<Standing> get5() {
            return _5;
        }

        public void set5(List<Standing> _5) {
            this._5 = _5;
        }

        public List<Standing> get6() {
            return _6;
        }

        public void set6(List<Standing> _6) {
            this._6 = _6;
        }

        public List<Standing> get7() {
            return _7;
        }

        public void set7(List<Standing> _7) {
            this._7 = _7;
        }

        public List<Standing> get8() {
            return _8;
        }

        public void set8(List<Standing> _8) {
            this._8 = _8;
        }

        public List<Standing> get9() {
            return _9;
        }

        public void set9(List<Standing> _9) {
            this._9 = _9;
        }
    }

    @Generated("org.jsonschema2pojo")
    public class UserId {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("nickname")
        @Expose
        private String nickname;
        @SerializedName("score")
        @Expose
        private Double score;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The _id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The nickname
         */
        public String getNickname() {
            return nickname;
        }

        /**
         *
         * @param nickname
         * The nickname
         */
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        /**
         *
         * @return
         * The score
         */
        public Double getScore() {
            return score;
        }

        /**
         *
         * @param score
         * The score
         */
        public void setScore(Double score) {
            this.score = score;
        }

    }

    public class LikeUpdate{
        public Boolean success;

    }
    public class ForecastUpdate{
        private Boolean isSuccess;
        private Boolean data;

        public Boolean getSuccess() {
            return isSuccess;
        }

        public void setSuccess(Boolean success) {
            isSuccess = success;
        }

        public Boolean getData() {
            return data;
        }

        public void setData(Boolean data) {
            this.data = data;
        }
    }

    public class Comment {

        @SerializedName("_id")
        @Expose
        private String id;



        @SerializedName("inserted_date")
        @Expose
        private String insertedDate;



        @SerializedName("forecast")
        @Expose
        private String forecast;

        private String reason;
        private Integer totalLike;

        public Integer getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(Integer totalLike) {
            this.totalLike = totalLike;
        }

        public String getForecast() {
            return forecast;
        }

        public void setForecast(String forecast) {
            this.forecast = forecast;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
            this.userId = userId;
        }

        @SerializedName("user_id")
        @Expose
        private UserId userId;
        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The _id
         */
        public void setId(String id) {
            this.id = id;
        }

        public String getInsertedDate() {
            return insertedDate;
        }

        public void setInsertedDate(String insertedDate) {
            this.insertedDate = insertedDate;
        }

    }

    public class LeagueStats {
        @SerializedName("goals")
        @Expose
        private List<LeagueDetailStat> goals = null;
        @SerializedName("asists")
        @Expose
        private List<LeagueDetailStat> asists = null;
        @SerializedName("yellow_cards")
        @Expose
        private List<LeagueDetailStat> yellowCards = null;
        @SerializedName("red_cards")
        @Expose
        private List<LeagueDetailStat> redCards = null;

        public List<LeagueDetailStat> getGoals() {
            return goals;
        }

        public void setGoals(List<LeagueDetailStat> goals) {
            this.goals = goals;
        }

        public List<LeagueDetailStat> getAsists() {
            return asists;
        }

        public void setAsists(List<LeagueDetailStat> asists) {
            this.asists = asists;
        }

        public List<LeagueDetailStat> getYellowCards() {
            return yellowCards;
        }

        public void setYellowCards(List<LeagueDetailStat> yellowCards) {
            this.yellowCards = yellowCards;
        }

        public List<LeagueDetailStat> getRedCards() {
            return redCards;
        }

        public void setRedCards(List<LeagueDetailStat> redCards) {
            this.redCards = redCards;
        }
    }
    public class LeagueDetailStat{
        @SerializedName("player_id")
        @Expose
        private String playerId;
        @SerializedName("nick")
        @Expose
        public String nick;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("team_id")
        @Expose
        private String teamId;
        @SerializedName("cc")
        @Expose
        private String cc;
        @SerializedName("team_name")
        @Expose
        private String teamName;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("year")
        @Expose
        private String year;
        @SerializedName("player_alias")
        @Expose
        private String playerAlias;
        @SerializedName("team_alias")
        @Expose
        private String teamAlias;
        @SerializedName("team_shield")
        @Expose
        private String teamShield;
        @SerializedName("player_image")
        @Expose
        private String playerImage;
        @SerializedName("team_flag")
        @Expose
        private String teamFlag;

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getPlayerAlias() {
            return playerAlias;
        }

        public void setPlayerAlias(String playerAlias) {
            this.playerAlias = playerAlias;
        }

        public String getTeamAlias() {
            return teamAlias;
        }

        public void setTeamAlias(String teamAlias) {
            this.teamAlias = teamAlias;
        }

        public String getTeamShield() {
            return teamShield;
        }

        public void setTeamShield(String teamShield) {
            this.teamShield = teamShield;
        }

        public String getPlayerImage() {
            return playerImage;
        }

        public void setPlayerImage(String playerImage) {
            this.playerImage = playerImage;
        }

        public String getTeamFlag() {
            return teamFlag;
        }

        public void setTeamFlag(String teamFlag) {
            this.teamFlag = teamFlag;
        }
    }
    public class Fixture{
        @SerializedName("leagueStatus")
        @Expose
        private LeagueStatus leagueStatus;
        @SerializedName("groups")
        @Expose
        private List<Group> groups = null;

        public LeagueStatus getLeagueStatus() {
            return leagueStatus;
        }

        public void setLeagueStatus(LeagueStatus leagueStatus) {
            this.leagueStatus = leagueStatus;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }

    public class LeagueStatus {

        @SerializedName("round")
        @Expose
        private Integer round;
        @SerializedName("total_rounds")
        @Expose
        private Integer totalRounds;
        @SerializedName("playoff")
        @Expose
        private String playoff;
        @SerializedName("phase")
        @Expose
        private String phase;
        @SerializedName("group_code")
        @Expose
        private String groupCode;

        public Integer getRound() {
            return round;
        }

        public void setRound(Integer round) {
            this.round = round;
        }

        public Integer getTotalRounds() {
            return totalRounds;
        }

        public void setTotalRounds(Integer totalRounds) {
            this.totalRounds = totalRounds;
        }

        public String getPlayoff() {
            return playoff;
        }

        public void setPlayoff(String playoff) {
            this.playoff = playoff;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }
    }

    public class Group {

        @SerializedName("group")
        @Expose
        private String group;
        @SerializedName("matches")
        @Expose
        private List<Match> matches = null;
        @SerializedName("league")
        @Expose
        private LeagueId league;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<Match> getMatches() {
            return matches;
        }

        public void setMatches(List<Match> matches) {
            this.matches = matches;
        }

        public LeagueId getLeague() {
            return league;
        }

        public void setLeague(LeagueId league) {
            this.league = league;
        }
    }


}
