package com.mrn.mobileappws.security;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
    // object to read properties files
    private final Environment env;

    public AppProperties(Environment env) {
        this.env = env;
    }

    // get the token secret from the properties file
    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }

}
