package com.lloyd.twitter;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by josediaz on 9/15/16.
 */
public class TweetUtilTest {

    @Test
    public void cleanSingleQuoteTest(){
        final String in0 = "Y'all should be cool about this tweet";
        final String in1 = "Look at all the single's out here...";

        final String exp0 = "Y''all should be cool about this tweet";
        final String exp1 = "Look at all the single''s out here...";

        String res0 = TweetUtil.cleanSingleQuote(in0);
        String res1 = TweetUtil.cleanSingleQuote(in1);

        assertTrue(res0.equals(exp0));
        assertTrue(res1.equals(exp1));
    }

    @Test
    public void cleanTweetTest(){
        final String in0 = "RT @john: Y'all should be cool about this tweet";
        final String in1 = "@Macey Look at all the single's out here...";

        final String exp0 = "Y''all should be cool about this tweet";
        final String exp1 = "Look at all the single''s out here...";

        String res0 = TweetUtil.clean(in0);
        String res1 = TweetUtil.clean(in1);

        assertTrue(res0.equals(exp0));
        assertTrue(res1.equals(exp1));
    }
}
