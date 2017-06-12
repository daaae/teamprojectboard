package edu.daae419skku.teamprojectboard;

/**
 * Created by daae0 on 2017-06-12.
 */
public class MemberNames {
    private String memberName;
    private String memberNum;

    public MemberNames(String memberName) {
        this.memberName = memberName.split("/")[0];
        this.memberNum = memberName.split("/")[1];
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberNum() {
        return this.memberNum;
    }

}
