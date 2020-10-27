package edu.byu.cs.tweeter.model.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class Status implements Comparable<Status> {
    private String message;
    private long whenPosted;
    private User author;

    private Status() {}

    public Status(String message, User author){
        this.message = message;
        this.author = author;
        this.whenPosted = Calendar.getInstance().getTimeInMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getWhenPosted() {
        return whenPosted;
    }

    public User getAuthor() {
        return author;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setWhenPosted(long whenPosted) {
        this.whenPosted = whenPosted;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return (author.equals(status.getAuthor())
                && message.equals(status.getMessage())
                && whenPosted == status.getWhenPosted());
    }

    @Override
    public String toString() {
        LocalDateTime postString =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(whenPosted),
                        TimeZone.getDefault().toZoneId());
        return "Status{" +
                "User='" + author.getAlias() + '\'' +
                ", date='" + postString + '\'' +
                ", message='" + message + '}';
    }

    @Override
    public int compareTo(Status status) {
        if (this.getWhenPosted() < status.getWhenPosted()){
            return -1;
        } else if (this.getWhenPosted() > status.getWhenPosted()){
            return 1;
        }
        return 0;
    }

}
