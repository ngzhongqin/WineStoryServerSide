package com.winestory.serverside.router;

/**
 * Created by zhongqinng on 15/6/15.
 */
public class UriPathHelper {
    public UriPathHelper(){

    }

    public UriPath getUriPath(String uriPathString){
        UriPath uriPath = UriPath.INVALID;
        if(uriPathString!=null){
            if(uriPathString.toLowerCase().equals("/login")){
                uriPath=UriPath.LOGIN;
            }
            if(uriPathString.toLowerCase().equals("/logout")){
                uriPath=UriPath.LOGOUT;
            }
            if(uriPathString.toLowerCase().equals("/signup")){
                uriPath=UriPath.SIGNUP;
            }
        }

        return uriPath;
    }
}
