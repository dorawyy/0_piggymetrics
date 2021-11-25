package com.piggymetrics.account.client;

import java.util.List;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.timeseries.DataPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author cdov
 */
@Component
public class StatisticsServiceClientFallback implements StatisticsServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServiceClientFallback.class);
    
    @Override
    public void updateStatistics(String accountName, Account account) {
        LOGGER.error("Error during update statistics for account: {}", accountName);
    }

    @Override
    public List<DataPoint> getStatisticsByAccountName(String accountName) {
        LOGGER.error("Error during get statistics for account: {}", accountName);
        return null; 
    }
}
