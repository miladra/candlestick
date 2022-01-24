package com.amazing.candlestick.repository;

import com.amazing.candlestick.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Integer> {

}
