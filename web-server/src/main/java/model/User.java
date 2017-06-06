package model;

import lombok.Data;

/**
 * Created by ndayan on 02/06/2017.
 */

@Data
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public User(){}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //    @JsonProperty("id")
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    @JsonProperty("firstName")
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    @JsonProperty("lastName")
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    @JsonProperty("email")
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
