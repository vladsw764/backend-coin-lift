package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.CryptoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoImageRepository extends JpaRepository<CryptoImage, Long> {
    CryptoImage findByCryptoId(Integer id);

    CryptoImage findCryptoImageByCryptoName(String name);
}
