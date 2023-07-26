package com.coinlift.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "crypto_images")
public class CryptoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cryptoId;

    private String cryptoImageUrl;

    private String cryptoName;

    public CryptoImage() {
    }

    public CryptoImage(Long id, Integer cryptoId, String cryptoImageUrl, String cryptoName) {
        this.id = id;
        this.cryptoId = cryptoId;
        this.cryptoImageUrl = cryptoImageUrl;
        this.cryptoName = cryptoName;
    }
}
