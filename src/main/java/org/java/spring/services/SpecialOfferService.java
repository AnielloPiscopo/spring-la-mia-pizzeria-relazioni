package org.java.spring.services;

import java.util.List;
import java.util.Optional;

import org.java.spring.pojo.SpecialOffer;
import org.java.spring.repo.SpecialOfferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialOfferService {
	@Autowired
	private SpecialOfferRepo repo;
	
	public List<SpecialOffer> findAll() {
		return repo.findAll();
}

	public List<SpecialOffer> findAllAvailableSpecialOffers() {
		return repo.findByDeletedFalse();
	}
	
	public List<SpecialOffer> findAllTrashedSpecialOffers() {
		return repo.findByDeletedTrue();
	}
	
	public Optional<SpecialOffer> findById(int id) {
		return repo.findById(id);
	}
	
	public SpecialOffer save(SpecialOffer specialOffer) {
		return repo.save(specialOffer);
	}
	
	public void delete(SpecialOffer specialOffer) {
		repo.delete(specialOffer);
	}
	
	public void deleteAll(List<SpecialOffer> specialOffers) {
		repo.deleteAll(specialOffers);
	}
}
