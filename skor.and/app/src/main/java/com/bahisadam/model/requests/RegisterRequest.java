package com.bahisadam.model.requests;

/**
 * Created by atata on 30/12/2016.
 */

public class RegisterRequest  extends  BaseAuthRequest {
    String email;
    String username;
    String password;

    public RegisterRequest(String email,  String user, String password) {
        super();
        this.email = email;
        this.password = password;
        this.username = user;
    }
}
