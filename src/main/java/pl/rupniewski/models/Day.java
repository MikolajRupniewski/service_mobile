package pl.rupniewski.models;

import java.io.Serializable;

public class Day implements Serializable {
    private Long id;
    private String open;
    private String close;

    public Day(Long id, String open, String close) {
        this.id = id;
        this.open = open;
        this.close = close;
    }

    public Day() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
