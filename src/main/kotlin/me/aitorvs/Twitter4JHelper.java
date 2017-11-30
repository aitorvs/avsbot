package me.aitorvs;

import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class Twitter4JHelper {
    private Twitter4JHelper() {
        throw new AssertionError("no instance");
    }

    public static void addStatusListner(TwitterStream stream, StatusListener listner) {
        stream.addListener(listner);
    }
}
