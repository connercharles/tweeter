package edu.byu.cs.tweeter.model.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class Status implements Comparable<Status> {
    private final String message;
    private final LocalDateTime whenPosted;
    private final User author;
    private final String url;


    public Status(String message, User author){
        this.message = message;
        this.author = author;
        this.whenPosted = LocalDateTime.now();
        this.url = "";
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

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(@Nullable Object o) {
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

    @NonNull
    @Override
    public String toString() {
        return "Status{" +
                "User='" + author.getAlias() + '\'' +
                ", date='" + whenPosted.toString() + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int compareTo(Status status) {
        return this.getWhenPosted().compareTo(status.getWhenPosted());
    }

}
