package com.bahisadam.model.requests;

import java.util.List;

/**
 * Created by atata on 29/12/2016.
 */

public class BaseAuthRequest {
    DeviceInfo deviceInfo;
    public  BaseAuthRequest(){
       deviceInfo = new DeviceInfo();
    }
    public class Response{
        public Boolean isSuccess;
        public List<String> favorites;
        public String error;
        public String username;

    }
}
