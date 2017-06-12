package edu.daae419skku.teamprojectboard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Project implements Serializable {

    private String projectName;
    private String date;
    private String leader;

    public Project() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public HashMap<String, String> toFirebaseObject() {
        HashMap<String, String> project = new HashMap<String, String>();
        project.put("projectName", projectName);
        project.put("date", date);
        project.put("leader", leader);

        return project;
    }
}
