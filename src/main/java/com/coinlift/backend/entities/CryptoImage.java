package com.coinlift.backend.entities;

import jakarta.persistence.*;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(Integer cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCryptoImageUrl() {
        return cryptoImageUrl;
    }

    public void setCryptoImageUrl(String cryptoImageUrl) {
        this.cryptoImageUrl = cryptoImageUrl;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }
}
