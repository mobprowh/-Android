package com.bahisadam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atata on 19/11/2016.
 */

public class LeagueMatchList {
    private MatchPOJO.LeagueId leauge;
    private List<MatchPOJO.Match> data;
    public LeagueMatchList(MatchPOJO.LeagueId leauge) {
        this.leauge = leauge;
        data = new ArrayList<MatchPOJO.Match>();
    }
    public MatchPOJO.LeagueId getLeauge() {
        return leauge;
    }

    public MatchPOJO.Match get(int i){
        return data.get(i);
    }
    public List<MatchPOJO.Match> getData() {
        return data;
    }

    public void add(MatchPOJO.Match match) {
        this.data.add(match);
    }

    public int size(){
        return data.size();
    }
}
