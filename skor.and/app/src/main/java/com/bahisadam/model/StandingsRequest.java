package com.bahisadam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by atata on 14/12/2016.
 */

public class StandingsRequest {
    public List<Standing> standings = null;

    public StandingsRequest() {
        this.standings = new LinkedList<Standing>();
    }

    public List<Standing> getStandings() {
        return standings;
    }

    public void setStandings(List<Standing> standings) {
        this.standings = standings;
    }


    public class Standing {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("groups")
        @Expose
        private List<Group> groups = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }

    public class Group {

        @SerializedName("team_standings")
        @Expose
        private List<TeamStanding> teamStandings = null;

        public List<TeamStanding> getTeamStandings() {
            return teamStandings;
        }

        public void setTeamStandings(List<TeamStanding> teamStandings) {
            this.teamStandings = teamStandings;
        }
    }

    public class Team {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public class TeamStanding {

        @SerializedName("team")
        @Expose
        private Team team;
        @SerializedName("rank")
        @Expose
        private Integer rank;
        @SerializedName("played")
        @Expose
        private Integer played;
        @SerializedName("win")
        @Expose
        private Integer win;
        @SerializedName("draw")
        @Expose
        private Integer draw;
        @SerializedName("loss")
        @Expose
        private Integer loss;
        @SerializedName("goals_for")
        @Expose
        private Integer goalsFor;
        @SerializedName("goals_against")
        @Expose
        private Integer goalsAgainst;
        @SerializedName("goal_diff")
        @Expose
        private Integer goalDiff;
        @SerializedName("points")
        @Expose
        private Integer points;
        @SerializedName("OM")
        @Expose
        private Integer OM;
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
        @SerializedName("_id")
        @Expose
        private Integer id;
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

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public Integer getPlayed() {
            return played;
        }

        public void setPlayed(Integer played) {
            this.played = played;
        }

        public Integer getWin() {
            return win;
        }

        public void setWin(Integer win) {
            this.win = win;
        }

        public Integer getDraw() {
            return draw;
        }

        public void setDraw(Integer draw) {
            this.draw = draw;
        }

        public Integer getLoss() {
            return loss;
        }

        public void setLoss(Integer loss) {
            this.loss = loss;
        }

        public Integer getGoalsFor() {
            return goalsFor;
        }

        public void setGoalsFor(Integer goalsFor) {
            this.goalsFor = goalsFor;
        }

        public Integer getGoalsAgainst() {
            return goalsAgainst;
        }

        public void setGoalsAgainst(Integer goalsAgainst) {
            this.goalsAgainst = goalsAgainst;
        }

        public Integer getGoalDiff() {
            return goalDiff;
        }

        public void setGoalDiff(Integer goalDiff) {
            this.goalDiff = goalDiff;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public Integer getOM() {
            return OM;
        }

        public void setOM(Integer oM) {
            this.OM = oM;
        }

        public Integer getG() {
            return g;
        }

        public void setG(Integer g) {
            this.g = g;
        }

        public Integer getB() {
            return b;
        }

        public void setB(Integer b) {
            this.b = b;
        }

        public Integer getM() {
            return m;
        }

        public void setM(Integer m) {
            this.m = m;
        }

        public Integer getAG() {
            return aG;
        }

        public void setAG(Integer aG) {
            this.aG = aG;
        }

        public Integer getYG() {
            return yG;
        }

        public void setYG(Integer yG) {
            this.yG = yG;
        }

        public Integer getAVG() {
            return aVG;
        }

        public void setAVG(Integer aVG) {
            this.aVG = aVG;
        }

        public Integer getPTS() {
            return pTS;
        }

        public void setPTS(Integer pTS) {
            this.pTS = pTS;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamNameTr() {
            return teamNameTr;
        }

        public void setTeamNameTr(String teamNameTr) {
            this.teamNameTr = teamNameTr;
        }

        public String getSeoName() {
            return seoName;
        }

        public void setSeoName(String seoName) {
            this.seoName = seoName;
        }

        public String getColor1() {
            return color1;
        }

        public void setColor1(String color1) {
            this.color1 = color1;
        }

        public String getColor2() {
            return color2;
        }

        public void setColor2(String color2) {
            this.color2 = color2;
        }
    }
}
