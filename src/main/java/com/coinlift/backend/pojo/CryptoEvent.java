package com.coinlift.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CryptoEvent {

    private String cryptoName;

    private String date;

    private String imageLink;

    private String link;

    @JsonProperty("is_conference")
    private boolean isConference;

    public CryptoEvent(String cryptoName, String date, String imageLink, String link, boolean isConference) {
        this.cryptoName = cryptoName;
        this.date = date;
        this.imageLink = imageLink;
        this.link = link;
        this.isConference = isConference;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isConference() {
        return isConference;
    }

    public void setConference(boolean conference) {
        isConference = conference;
    }
}
