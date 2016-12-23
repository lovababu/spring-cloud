package org.avol.springcloud.stream.kafka.model;

import java.util.Date;

/**
 * Created by dpadal on 12/23/2016.
 */
public class Tweet {

    private String name;
    private String tweet;
    private Date time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "name='" + name + '\'' +
                ", tweet='" + tweet + '\'' +
                ", time=" + time +
                '}';
    }
}
