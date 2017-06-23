package edu.daae419skku.teamprojectboard;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by daae0 on 2017-06-17.
 */

public class Todo implements Serializable {

    private String todoName;
    private String date;
    private String person;
    private String memo;
    private int state;
    private String key;
    private String projectKey;


    public Todo() {


    }

    public String getMemo() {return memo;}

    public void setMemo(String memo) {this.memo = memo;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getState() {return state;}

    public void setState(int state) { this.state = state;}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getProjectKey() {return projectKey;}

    public void setProjectKey(String projectKey) {this.projectKey = projectKey;}

}
