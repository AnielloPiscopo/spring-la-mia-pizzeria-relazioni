package org.java.spring.services;

import org.java.spring.repo.SpecialOfferRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class SpecialOfferService {
	@Autowired
	private SpecialOfferRepo repo;
}
