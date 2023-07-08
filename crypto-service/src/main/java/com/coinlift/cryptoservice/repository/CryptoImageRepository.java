package com.coinlift.cryptoservice.repository;

import com.coinlift.cryptoservice.model.CryptoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoImageRepository extends JpaRepository<CryptoImage, Long> {
    CryptoImage findByCryptoId(Integer id);
    CryptoImage findCryptoImageByCryptoName(String name);
}
