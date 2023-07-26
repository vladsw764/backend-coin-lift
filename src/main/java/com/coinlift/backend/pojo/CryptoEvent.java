package com.coinlift.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
