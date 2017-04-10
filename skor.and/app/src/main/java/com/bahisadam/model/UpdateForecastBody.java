package com.bahisadam.model;

/**
 * Created by atata on 29/11/2016.
 */

public class UpdateForecastBody {
    String match_id;
    String key;



    public UpdateForecastBody(String matchId, String key) {
        this.match_id = matchId;
        this.key = key;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
