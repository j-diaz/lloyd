package com.lloyd.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains methods for cleaning up tweets of special characters.
 */
public class TweetUtil {

    private static final Logger logger = LoggerFactory.getLogger(TweetUtil.class);
    private final static Pattern ESCAPE_SINGLE_QUOTE_REGEX = Pattern.compile("(')");
    private final static Pattern MENTIONS_SYNTAX_REGEX = Pattern.compile("(@)\\w+");

    /**
     * Should escape single quotes with backslash for proper sql insertion
     * @param text
     * @return
     */
    public static String cleanSingleQuote(String text){
        Matcher matcher = ESCAPE_SINGLE_QUOTE_REGEX.matcher(text);
        //for now...
        String res = matcher.replaceAll("''");
        logger.debug("Clean: "+res);
        return res;
    }

    public static String clean(String text){

        text = TweetUtil.cleanSingleQuote(text);

        if(text.startsWith("RT @")){
            int i = text.indexOf(":");
            text = text.substring(i + 1, text.length()).trim();
        }

        if(text.startsWith("@")){ // Case of replies
            int i = text.indexOf(" ");
            text = text.substring(i + 1, text.length()).trim();
        }

        if(text.contains("@")){
            Matcher matcher = MENTIONS_SYNTAX_REGEX.matcher(text);
            String res = matcher.replaceAll("");
            text = res;
        }

        text = text.trim();

        return text;
    }

}
