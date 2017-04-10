package com.bahisadam.model.requests;

/**
 * Created by atata on 29/12/2016.
 */

public class UserLoginRequest extends BaseAuthRequest  {

    public String username;
    public String password;

    public UserLoginRequest( String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
}
