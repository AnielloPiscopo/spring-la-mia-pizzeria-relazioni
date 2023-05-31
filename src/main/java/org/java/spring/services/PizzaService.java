package org.java.spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.java.spring.pojo.Pizza;
import org.java.spring.repo.PizzaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class PizzaService {
	@Autowired
	private  PizzaRepo repo;
	
	private List<Pizza> getPizzasWithRelations(List<Pizza> pizzas) {
		List<Pizza> pizzasWithRelations = new ArrayList<>();
		
		for(Pizza pizza : pizzas){
			Hibernate.initialize(pizza.getSpecialOffers());
			pizzasWithRelations.add(pizza);
		}
		
		return pizzasWithRelations;
	};
	
	private Optional<Pizza> getPizzaWithRelantions(Optional<Pizza> pizzaOpt){
		Hibernate.initialize(pizzaOpt.get().getSpecialOffers());
		return pizzaOpt;
	}
	
	public List<Pizza> findAll() {
			return repo.findAll();
	}
	
	public List<Pizza> findAllAvailablePizzas() {
		return repo.findByDeletedFalse();
	}
	
	public List<Pizza> findAllTrashedPizzas() {
		return repo.findByDeletedTrue();
	}
	
	public List<Pizza> filterByName(String name) {
		return repo.findByNameContaining(name);
	}
	
	public List<Pizza> filterByNameForAvailablePizzas(String name) {
		return repo.findByNameContainingAndDeletedFalse(name);
	}
	
	public List<Pizza> filterByNameForTrashedPizzas(String name) {
		return repo.findByNameContainingAndDeletedTrue(name);
	}
	
	public Optional<Pizza> findById(int id) {
		return repo.findById(id);
	}
	
	@Transactional
	public List<Pizza> findAllWithRel(){
		List<Pizza> pizzas = repo.findAll();
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public List<Pizza> findAllAvailablePizzasWithRel() {
		List<Pizza> pizzas = repo.findByDeletedFalse();
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public List<Pizza> findAllTrashedPizzasWithRel() {
		List<Pizza> pizzas = repo.findByDeletedTrue();
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public List<Pizza> filterByNameWithRel(String name) {
		List<Pizza> pizzas = repo.findByNameContaining(name);
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public List<Pizza> filterByNameForAvailablePizzasWithRel(String name) {
		List<Pizza> pizzas = repo.findByNameContainingAndDeletedFalse(name);
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public List<Pizza> filterByNameForTrashedPizzasWithRel(String name) {
		List<Pizza> pizzas = repo.findByNameContainingAndDeletedTrue(name);
		return getPizzasWithRelations(pizzas);
	}
	
	@Transactional
	public Optional<Pizza> findByIdWithRel(int id) {
		Optional<Pizza> pizzaOpt = repo.findById(id);
		return getPizzaWithRelantions(pizzaOpt);
	}
	
	public Pizza save(Pizza pizza) {
		return repo.save(pizza);
	}
	
	public void delete(Pizza pizza) {
		repo.delete(pizza);
	}
	
	public void deleteAll(List<Pizza> pizzas) {
		repo.deleteAll(pizzas);
	}
}
