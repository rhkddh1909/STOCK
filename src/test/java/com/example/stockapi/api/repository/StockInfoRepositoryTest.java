package com.example.stockapi.api.repository;

import com.example.stockapi.api.repository.domain.StockInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StockInfoRepositoryTest {
    @Autowired
    StockInfoRepository testStockInfoRepository;

    @Test
    public void selectStockALlInfoTest(){
        List<StockInfo> stockInfoList = testStockInfoRepository.findAll().orElse(List.of());
        assertThat(stockInfoList).isNotEmpty();
    }
}