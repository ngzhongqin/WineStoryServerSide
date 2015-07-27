package com.winestory.serverside.framework;

import java.util.UUID;

/**
 * Created by zhongqinng on 27/7/15.
 * UUIDGenerator - generate Unique IDs for sessions
 */
public class UUIDGenerator {
    public UUIDGenerator(){}

    public String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
