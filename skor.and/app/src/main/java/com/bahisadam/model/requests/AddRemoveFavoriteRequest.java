package com.bahisadam.model.requests;

/**
 * Created by atata on 04/01/2017.
 */

public class AddRemoveFavoriteRequest {

    public String match_id;

    public AddRemoveFavoriteRequest(String match_id) {
        this.match_id = match_id;
    }

    public class Response{
        public String error;
        public String errorType;
    }
}
