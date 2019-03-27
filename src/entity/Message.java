package entity;


import java.sql.Time;

public class Message {
    private int to;
    private int from;
    private String msg;
    private Double time;

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
