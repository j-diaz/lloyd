CREATE TABLE Tweet (
  tweet_id bigserial,
  user_id VARCHAR (200),
  text text,
  retweets bigint,
  favorites bigint,
  time TIMESTAMP,
  emitted_count NUMERIC,
  PRIMARY KEY (tweet_id)
);

CREATE TABLE Tag (
  tag_id bigserial,
  tag_description varchar(255),
  tag_name varchar(255),
  PRIMARY KEY (tag_id)
);

CREATE TABLE Evidence (
  evidence_id bigserial,
  recommendation_id bigint,
  event_id bigint,
  time TIMESTAMP,
  PRIMARY KEY (evidence_id)
);

CREATE TABLE Recommendation (
 recommendation_id bigserial,
 tweet_id bigint,
 time TIMESTAMP,
 PRIMARY KEY (recommendation_id),
 FOREIGN KEY (tweet_id) REFERENCES Tweet
);
