package com.amazing.candlestick.repository;

import com.amazing.candlestick.entity.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface InstrumentRepository extends JpaRepository<InstrumentEntity, Integer> {

    Optional<InstrumentEntity> findTopByIsinOrderByDate(@Param("isin") String isin);
}
