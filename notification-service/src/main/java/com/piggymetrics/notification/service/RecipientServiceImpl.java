package com.piggymetrics.notification.service;

import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.repository.RecipientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Service
public class RecipientServiceImpl implements RecipientService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RecipientRepository repository;

	@Override
	public Recipient findByAccountName(String accountName) {
		Assert.hasLength(accountName);
		return repository.findByAccountName(accountName); // call, missing, repo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Recipient save(String accountName, Recipient recipient) {

		recipient.setAccountName(accountName); // call
		recipient.getScheduledNotifications().values() // call 
				.forEach(settings -> {
					if (settings.getLastNotified() == null) { // call, found as call from lambda$save$0
						settings.setLastNotified(new Date()); // call, found as call from lambda$save$0
					}
				});

		repository.save(recipient); // call, missing, repo

		log.info("recipient {} settings has been updated", recipient);

		return recipient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Recipient> findReadyToNotify(NotificationType type) { 
		switch (type) {
			case BACKUP:
				return repository.findReadyForBackup(); // call, missing, repo
			case REMIND:
				return repository.findReadyForRemind(); // call, missing, repo
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markNotified(NotificationType type, Recipient recipient) {
		recipient.getScheduledNotifications().get(type).setLastNotified(new Date()); // call // call
		repository.save(recipient); // call, missing, repo
	}
}
