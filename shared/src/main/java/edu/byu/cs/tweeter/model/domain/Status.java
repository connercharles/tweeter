package edu.byu.cs.tweeter.model.domain;

import java.time.LocalDateTime;

public class Status implements Comparable<Status> {
    private final String message;
    private final LocalDateTime whenPosted;
    private final User author;


    public Status(String message, User author){
        this.message = message;
        this.author = author;
        this.whenPosted = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getWhenPosted() {
        return whenPosted;
    }

    public User getAuthor() {
        return author;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return (author.equals(status.getAuthor())
                && message.equals(status.getMessage())
                && whenPosted.getDayOfMonth() == (status.getWhenPosted().getDayOfMonth())
                && whenPosted.getHour() == (status.getWhenPosted().getHour())
                && whenPosted.getMinute() == (status.getWhenPosted().getMinute())
        );
    }

    @Override
    public String toString() {
        return "Status{" +
                "User='" + author.getAlias() + '\'' +
                ", date='" + whenPosted.toString() + '\'' +
                ", message='" + message + '}';
    }

    @Override
    public int compareTo(Status status) {
        return this.getWhenPosted().compareTo(status.getWhenPosted());
    }

}
