package com.piggymetrics.account.service;

import com.piggymetrics.account.client.AuthServiceClient;
import com.piggymetrics.account.client.StatisticsServiceClient;
import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.Currency;
import com.piggymetrics.account.domain.Saving;
import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.domain.timeseries.DataPoint;
import com.piggymetrics.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private StatisticsServiceClient statisticsClient;

	@Autowired
	private AuthServiceClient authClient;

	@Autowired
	private AccountRepository repository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findByName(String accountName) {
		Assert.hasLength(accountName);
		return repository.findByName(accountName); // call, missing, repo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account create(User user) {

		Account existing = repository.findByName(user.getUsername()); // call, missing, repo // call 
		Assert.isNull(existing, "account already exists: " + user.getUsername()); // call

		authClient.createUser(user); // call, missing, openfeign

		Saving saving = new Saving(); // call
		saving.setAmount(new BigDecimal(0)); // call
		saving.setCurrency(Currency.getDefault()); //call // call
		saving.setInterest(new BigDecimal(0)); // call 
		saving.setDeposit(false); // call
		saving.setCapitalization(false); // call

		Account account = new Account(); // call
		account.setName(user.getUsername()); // call // call
		account.setLastSeen(new Date()); // call 
		account.setSaving(saving); // call

		repository.save(account); // call, missing, repo

		log.info("new account has been created: " + account.getName()); // call

		return account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveChanges(String name, Account update) {

		Account account = repository.findByName(name); // call, missing, repo
		Assert.notNull(account, "can't find account with name " + name);

		account.setIncomes(update.getIncomes()); // call // call 
		account.setExpenses(update.getExpenses()); // call // call 
		account.setSaving(update.getSaving()); // call // call 
		account.setNote(update.getNote()); // call // call 
		account.setLastSeen(new Date()); // call 
		repository.save(account); // call, missing, repo

		log.debug("account {} changes has been saved", name);

		statisticsClient.updateStatistics(name, account); // call, missing, openfeign (2 calls, could be to accountService or to statisticsAccountService)
		// get
		// compare
		List<DataPoint> statDataPoints = statisticsClient.getStatisticsByAccountName(name);
	}
}
