package com.lloyd.core;
//
//import com.lloyd.backend.Backend;
//import com.lloyd.backend.BackendResult;
//import com.lloyd.backend.BackendResultSet;
import com.scriptfuzz.backend.*;
import com.lloyd.twitter.TweetCollector;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Represents an abstract tweet recommender
 */
public abstract class TweetRecommender {

    protected volatile TweetCollector collector;
    protected volatile Backend backend;
    /**
     * Tweet recommender
     * @throws IOException
     */
    public TweetRecommender(String[] filters) throws IOException {
        collector = new TweetCollector.Builder()
                .backend("pg", "database.properties")
                .runInBackground(true)
                .tweetPerSec(1)
                .filters(filters)
                .build();

        backend = collector.getBackend();
    }

    /**
     * Gets the next recommendation. The algorithms is dead simple right now.
     * Get all Tweets stored, select one at random, update its emitted_count
     * @return Tweet to post.
     */
    protected synchronized  String getRecommendation(){

        final String query = "SELECT * FROM Tweet";
        BackendResultSet brs = backend.read(query, null);

        Random random = new Random();
        int selected = random.nextInt(brs.getCount() - 1);
        System.out.println("selected: "+selected);
        List<BackendResult> resultSet = brs.getRows();

        BackendResult selectedRow = resultSet.get(selected);

        long id = selectedRow.getLong("tweet_id");
        String text = selectedRow.getString("text");
        int emitted_count = selectedRow.getInt("emitted_count");

        // update emitted count
        final String update = "UPDATE TWEET"
                              +" SET emitted_count = " + (emitted_count + 1)
                              +" WHERE tweet_id = " + id;

        // Don't use the result :)
        brs = backend.update(update, null);

        return text;
    }

}
