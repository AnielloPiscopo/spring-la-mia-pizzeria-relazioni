package org.java.spring.services;

import java.util.List;
import java.util.Optional;

import org.java.spring.pojo.Pizza;
import org.java.spring.repo.PizzaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PizzaService {
	@Autowired
	private  PizzaRepo pizzaRepo;
	
	public List<Pizza> findAll() {
			return pizzaRepo.findAll();
	}
	
	public List<Pizza> findAllAvailablePizzas() {
		return pizzaRepo.findByDeletedFalse();
	}
	
	public List<Pizza> findAllTrashedPizzas() {
		return pizzaRepo.findByDeletedTrue();
	}
	
	public Pizza save(Pizza pizza) {
		return pizzaRepo.save(pizza);
	}
	
	public Optional<Pizza> findById(int id) {
		return pizzaRepo.findById(id);
	}
	
	public List<Pizza> filterByName(String name) {
		return pizzaRepo.findByNameContaining(name);
	}
	
	public List<Pizza> filterByNameForAvailablePizzas(String name) {
		return pizzaRepo.findByNameContainingAndDeletedFalse(name);
	}
	
	public List<Pizza> filterByNameForTrashedPizzas(String name) {
		return pizzaRepo.findByNameContainingAndDeletedTrue(name);
	}
	
	public void delete(Pizza pizza) {
		pizzaRepo.delete(pizza);
	}
	
	public void deleteAll(List<Pizza> pizzas) {
		pizzaRepo.deleteAll(pizzas);
	}
}
