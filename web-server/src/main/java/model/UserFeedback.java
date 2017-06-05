package model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by ndayan on 03/06/2017.
 */
@Data
public class UserFeedback {

    private String userId;
    private Timestamp timestamp;
    private ActivityType activityType;

    public enum ActivityType {
        BLOCK_DOMAIN(0),
        THUMBS_UP(1),
        THUMBS_DOWN(2);

        private final int value;

        ActivityType (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };
}
