package com.lloyd.twitter;

import com.scriptfuzz.backend.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder Pattern
 * Fetches filtered tweets from the public twitter API
 */
public class TweetCollector {

    private static final Logger logger = LoggerFactory.getLogger(TweetCollector.class);

    private static final int MIN_NUMBER_RETWEET = 1;
    private static final int MIN_NUMBER_FAVORITE = 1;

    private final TwitterStream tweetStream;
    private final Backend backend;
    private final int tps;
    private final String[] filters;
    private final boolean background;

    public static class Builder {
        private Backend backend;
        private int tps;
        private String[] filters;
        private boolean background;

        // Default a twitter tweetStream
        private TwitterStream stream = TwitterStreamFactory.getSingleton();

        public Builder(){ }

        public Builder(String backendType, String path, int freq) throws IOException {
            backend = BackendFactory.getBackend(backendType, path);
            tps = freq;
        }

        public Builder filters(String[] f){
            filters = f;
            return this;
        }

        public Builder tweetPerSec(int freq){
            tps = freq;
            return this;
        }

        public Builder backend(String db, String path) throws IOException {
            backend = BackendFactory.getBackend(db, path);
            return this;
        }

        public Builder runInBackground(boolean flag){
            background = flag;
            return this;
        }

        public TweetCollector build(){
            return new TweetCollector(this);
        }
    }

    private TweetCollector(Builder builder){
        tweetStream = builder.stream;
        backend = builder.backend;
        tps = builder.tps;
        filters = builder.filters;
        background = builder.background;
    }


    /**
     * Listens and stores tweets matching filters provides
     */
    public void start(){
        logger.info("Collecting tweets...");
        // TODO: Non-blocking
        if(background) {

            tweetStream.onStatus(tweet -> storeTweet(tweet))
                    .onException(ex -> logger.error("Error reading tweet: " + ex.getMessage()))
                    .filter(filters);

        }else{
            // TODO: Blocking
        }
    }

    public Backend getBackend(){
        return backend;
    }

    // TODO: Implement stop method

    /**
     * Stores a tweet into our database
     * @param tweet A tweet status object
     * @return whether or not it successfully stored the tweet
     */
    private void storeTweet(Status tweet){
        // log event
        final String user = tweet.getUser().getScreenName();
        final String text = TweetUtil.clean( tweet.getText() );
        final int fav = tweet.getFavoriteCount();
        final int ret = tweet.getRetweetCount();
        final Date time = tweet.getCreatedAt();
        // For now log tweets
        logger.info("@%{} {} | fav = {}, ret = {}", user, text, fav, ret);

            logger.info("@%{} {} ", user, text);

            //logger.info("Tweet has more than 10 favorites");
            Map<String, Object> values = new HashMap<>();
            values.put("user_id", user);
            values.put("text", text);
            values.put("retweets", ret);
            values.put("favorites", fav);
            values.put("time", time);
            values.put("emitted_count", 0);

            BackendResultSet drs = backend.insert(
                "INSERT INTO TWEET VALUES (default,'"+user+"','"+text+"',"+ret+","+fav+",'"+time+"',0)", values);
            // Clean up data
    }

    // Example runner, temporary
    public static void main(String[] args) throws IOException {
        TweetCollector collector = new Builder()
                .backend("pg", "database.properties")
                .filters(args)
                .tweetPerSec(100)
                .runInBackground(true)
                .build();

        collector.start();

    }
}