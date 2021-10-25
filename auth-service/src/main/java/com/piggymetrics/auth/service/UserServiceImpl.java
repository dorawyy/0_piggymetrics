package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository repository;

	@Override
	public void create(User user) {

		Optional<User> existing = repository.findById(user.getUsername()); // call // call 
		existing.ifPresent(it-> {throw new IllegalArgumentException("user already exists: " + it.getUsername());}); // call from lambda$create$0

		String hash = encoder.encode(user.getPassword()); // call 
		user.setPassword(hash); // call com.piggymetrics.auth.domain.User

		repository.save(user); // call com.piggymetrics.auth.repository.UserRepository

		log.info("new user has been created: {}", user.getUsername()); // call
	}
}
