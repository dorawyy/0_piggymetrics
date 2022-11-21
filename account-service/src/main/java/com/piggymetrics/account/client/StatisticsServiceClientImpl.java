/*
package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.timeseries.DataPoint;
import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.Item;
import com.piggymetrics.account.domain.Saving;
import com.piggymetrics.account.domain.StatAccount;
import com.piggymetrics.account.domain.StatSaving;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import com.piggymetrics.statistics.controller.StatisticsController;


public class StatisticsServiceClientImpl implements StatisticsServiceClient {

	public StatisticsController statController;

	@Override
	public void updateStatistics(String accountName, Account account){
		StatAccount acc = convertAccountFromAccuntToStatistics(account);
		// make the call with the acc
		statController(accountName, acc);
		return;

	}


	public StatAccount convertAccountFromAccuntToStatistics(Account account) {
		StatAccount toAccount = new StatAccount();
		toAccount.setExpenses(account.getExpenses());
		toAccount.setIncomes(account.getIncomes());
		toAccount.setName(account.getName());
		StatSaving toSaving = convertSavingFromAccuntToStatistics(account.getSaving());
		toAccount.setSaving(toSaving);
		return toAccount;
	}


	public StatSaving convertSavingFromAccuntToStatistics(Saving saving) {
		StatSaving toSaving = new StatSaving();
		toSaving.setAmount(saving.getAmount());
		toSaving.setCapitalization(saving.getCapitalization());
		toSaving.setCurrency(saving.getCurrency());
		toSaving.setDeposit(saving.getDeposit());
		toSaving.setInterest(saving.getInterest());
		return toSaving;
	}

	@Override
	public List<DataPoint> getStatisticsByAccountName(String accountName){

		return new ArrayList<DataPoint>();

	}
}

*/