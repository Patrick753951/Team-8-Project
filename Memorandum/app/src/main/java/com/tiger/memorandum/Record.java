package com.tiger.memorandum;


public class Record {
    private long id;
    private int time;
    private int type;
    private int num;
    private int spaceTime;
    private int curNum;
    private String person;
    private String text;

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getSpaceTime() {
        return spaceTime;
    }

    public void setSpaceTime(int spaceTime) {
        this.spaceTime = spaceTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", time=" + time +
                ", type=" + type +
                ", num=" + num +
                ", spaceTime=" + spaceTime +
                ", curNum=" + curNum +
                ", person='" + person + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
