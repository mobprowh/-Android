package com.bahisadam.model.requests;

/**
 * Created by atata on 30/12/2016.
 */

public class UpdatePredictionRequest  {
    public String forecast;
    public String match_id;
    public String reason;

    public UpdatePredictionRequest(String forecast, String match_id, String reason) {
        this.forecast = forecast;
        this.match_id = match_id;
        this.reason = reason;
    }

    public class Response{
        public Boolean isSuccessful;
        public String error;
        public String errorType;
        public Integer ok;
        public String electionId;
        public Integer n;

    }
}
