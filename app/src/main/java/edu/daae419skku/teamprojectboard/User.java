package edu.daae419skku.teamprojectboard;

public class User {

    public String username;
    public String usernum;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String usernum) {
        this.username = username;
        this.usernum = usernum;
    }

}
