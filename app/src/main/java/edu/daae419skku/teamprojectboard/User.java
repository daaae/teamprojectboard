package edu.daae419skku.teamprojectboard;

public class User {

    public String username;
    public String usernum;
    public String userreg;
    public String userProjects;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String usernum, String userreg) {
        this.username = username;
        this.usernum = usernum;
        this.userreg = userreg;
    }

    public User(String userreg) {
        this.userreg = userreg;
    }

   /* public User(String newproject) {
        this.userProjects += newproject + ",";
    }
*/
}
