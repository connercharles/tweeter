package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class PagedResults<T> {

    /**
     * The data values returned in this page of results
     */
    private List<T> values;

    /**
     * The last value returned in this page of results
     * This value is typically included in the query for the next page of results
     */
    private String lastKey;

    public PagedResults() {
        values = new ArrayList<>();
        lastKey = null;
    }

    // Values property

    public void addValue(T v) {
        values.add(v);
    }

    public boolean hasValues() {
        return (values != null && values.size() > 0);
    }

    public List<T> getValues() {
        return values;
    }

    // LastKey property

    public void setLastKey(String value) {
        lastKey = value;
    }

    public String getLastKey() {
        return lastKey;
    }

    public boolean hasLastKey() {
        return (lastKey != null && lastKey.length() > 0);
    }
}
