package com.lloyd.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.IOException;
import java.util.Random;

/**
 * Created by jdiaz on 9/19/16.
 */
public class Lloyd extends TweetRecommender implements Bot {

    private Logger logger = LoggerFactory.getLogger(Lloyd.class);

    private static final long MAX_EMIT_SLEEP_TIME    = 1800000; // 30min
    private static final long MIN_EMIT_SLEEP_TIME    = 900000;  // 15min
    private static Random random = new Random();

    public Lloyd() throws IOException {
        super(new String[]{"#interesting", "#fact", "#life", "#adventure", "#happiness"});
    }

    @Override
    public void reply() {
        //Todo
    }

    @Override
    public void emit() {
        Thread thread = new Thread(() -> {
            Twitter twitter = TwitterFactory.getSingleton();
            try {
                while(true){
                    long tweetAfterMillis = random.nextInt( (int) ((MAX_EMIT_SLEEP_TIME - MIN_EMIT_SLEEP_TIME) + 1)) + MIN_EMIT_SLEEP_TIME ;
                    logger.info("Emitter going to sleep for " +  (tweetAfterMillis / 60000)  + "min");
                    Thread.sleep(tweetAfterMillis);
                    String tweetText = super.getRecommendation();
                    logger.info("Emitter tweet update: "+tweetText);
                    Status status = twitter.updateStatus(tweetText);
                }
            } catch (TwitterException e) {
                logger.error(e.getMessage());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        });

        logger.info("Creating emitter thread...");
        thread.start();
    }

    private void collect(){

        Thread thread = new Thread(() -> super.collector.start());
        logger.info("Creating collector thread...");
        thread.start();
    }

    public void chat() {
        logger.info("Started lloyd.");

       // collect();
        reply();
        emit();
    }

    public static void main(String[] args) throws IOException {
        Lloyd lloydBot = new Lloyd();
        lloydBot.chat();
    }

}
