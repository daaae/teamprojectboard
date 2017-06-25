package edu.daae419skku.teamprojectboard;

public class User {

    public String username, useremail, usernum, userreg;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String usernum, String useremail, String userreg) {
        this.username = username;
        this.usernum = usernum;
        this.useremail = useremail;
        this.userreg = userreg;
    }

    public User(String userreg) {
        this.userreg = userreg;
    }

}
