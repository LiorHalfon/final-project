-- THIS FILE IS NOT IN USE
CREATE TABLE users (
  id      INTEGER       GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1) PRIMARY KEY,
  lname   VARCHAR(30)   NOT NULL,
  fname   VARCHAR(30)   NOT NULL,
  email   VARCHAR(50)   NOT NULL    UNIQUE
);

CREATE TABLE UserFeedback (
  userId        INTEGER,
  timestamp     TIMESTAMP   NOT NULL,
  activityType  VARCHAR(30) NOT NULL,
  FOREIGN KEY (userId) REFERENCES users(id)
);