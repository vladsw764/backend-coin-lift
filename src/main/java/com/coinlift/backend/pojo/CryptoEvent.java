package com.coinlift.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CryptoEvent {
    private String cryptoName;
    private String date;
    private String imageLink;
    @JsonProperty("is_conference")
    private boolean isConference;
}
