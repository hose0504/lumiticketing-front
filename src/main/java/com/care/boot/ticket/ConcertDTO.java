package com.care.boot.ticket;

import java.io.Serializable;

public class ConcertDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 관리용

    private int concert_id;
    private String name;
    private String genre;
    private String location;
    private String date; // 또는 java.sql.Date

    // Getter / Setter
    public int getConcert_id() {
        return concert_id;
    }
    public void setConcert_id(int concert_id) {
        this.concert_id = concert_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
