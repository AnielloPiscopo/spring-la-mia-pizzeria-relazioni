package org.java.spring.controller;

import java.util.List;
import java.util.Optional;

import org.java.spring.pojo.Pizza;
import org.java.spring.services.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
	private static String pageTitle;
	
	private String getPizzas(List<Pizza> pizzas , String title , String template , Model model) {
		model.addAttribute("pizzas" , pizzas);
		model.addAttribute("title" , title);
		return template;
	}
	
	private String saveInDb(Pizza pizza , BindingResult br , String templateToEdit , String templateToRedirect , String title , String btnText , Model model) {
		if(br.hasErrors()) {
			model.addAttribute("pizza" , pizza);
			model.addAttribute("errors" , br);
			modifyOrCreatePizza(pizza, title, btnText, templateToRedirect, model);
			return templateToEdit;
		}
		
		pizzaService.save(pizza);
		return templateToRedirect;
	}
	
	private String modifyOrCreatePizza(Pizza pizza , String title , String btnText , String template , Model model) {
		model.addAttribute("btnText" , btnText);
		model.addAttribute("pizza", pizza);
		model.addAttribute("title" , title);
		return template;
	}
	
	private String changeTheDeletedValue(int id , boolean trashed) {
		Optional<Pizza> optPizza = pizzaService.findById(id);
		Pizza pizza = optPizza.get();
		pizza.setDeleted(trashed);
		pizzaService.save(pizza);
		return "redirect:/pizzas/";
	}
	
	@Autowired
	private PizzaService pizzaService;
	
	@GetMapping("/")
	public String index(Model model ) {
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		return getPizzas(pizzas , "Lista pizze" , "pizza/index" , model);
	}
	
	@PostMapping("/")
	public String index(Model model , @RequestParam(name = "name") String name) {
		List<Pizza> pizzas = pizzaService.filterByNameForAvailablePizzas(name);
		return getPizzas(pizzas , "Lista pizze" , "pizza/index" , model);
	}
	
	@GetMapping("/{id}")
	public String show(Model model , @PathVariable("id") int id) {
		Optional<Pizza> optPizza = pizzaService.findById(id);
		Pizza pizza = optPizza.get();
		pageTitle = "Pizza " + pizza.getName();
		model.addAttribute("pizza" , pizza);
		model.addAttribute("title" , pageTitle);
		return "pizza/show";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		return modifyOrCreatePizza(new Pizza() , "Creazione pizza" , "Aggiungi alla lista la pizza" , "pizza/create" , model);
	}
	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza pizza , BindingResult br , Model model) {
		return saveInDb(pizza, br , "pizza/create" ,  "redirect:/pizzas/" , "Creazione pizza" , "Aggiungi alla lista la pizza" , model);
	}
	
	@GetMapping("/edit/{id}")
	public String edit(Model model , @PathVariable("id") int id) {
		Optional<Pizza> optPizza = pizzaService.findById(id);
		Pizza pizza = optPizza.get();
		pageTitle = "Modifica la pizza: " + pizza.getName();
		return modifyOrCreatePizza(pizza , pageTitle , "Modifica elemento" , "pizza/edit" , model);
	}
	
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("pizza") Pizza pizza , BindingResult br , Model model) {
		pageTitle = "Modifica la pizza: " + pizza.getName();
		return saveInDb(pizza, br , "pizza/edit" , "redirect:/pizzas/" + pizza.getId() , pageTitle , "Modifica elemento" , model);
	}
	
	@PostMapping("/soft-delete/{id}")
	public String softDelete(@PathVariable("id") int id) {
		return changeTheDeletedValue(id, true);
	}
	
	@PostMapping("/soft-delete-all")
	public String softDeleteAll() {
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		for(Pizza pizza : pizzas) {
			pizza.setDeleted(true);
			pizzaService.save(pizza);
		}
		return "redirect:/pizzas/";
	}
	
	@GetMapping("/trash")
	public String trash(Model model ) {
		List<Pizza> pizzas = pizzaService.findAllTrashedPizzas();
		return getPizzas(pizzas , "Lista pizze cestinate" , "pizza/trash" , model);
	}
	
	@PostMapping("/trash")
	public String trash(Model model , @RequestParam(name = "name") String name) {
		List<Pizza> pizzas = pizzaService.filterByNameForTrashedPizzas(name);
		return getPizzas(pizzas , "Lista pizze cestinate" , "pizza/trash" , model);
	}
	
	@PostMapping("/refresh/{id}")
	public String refresh(@PathVariable("id") int id) {
		return changeTheDeletedValue(id, false);
	}
	
	@PostMapping("/refresh-all")
	public String refreshAll() {
		List<Pizza> pizzas = pizzaService.findAllTrashedPizzas();
		for(Pizza pizza : pizzas) {
			pizza.setDeleted(false);
			pizzaService.save(pizza);
		}
		return "redirect:/pizzas/trash";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		Optional<Pizza> optPizza = pizzaService.findById(id);
		Pizza pizza = optPizza.get();
		pizzaService.delete(pizza);
		return "redirect:/pizzas/trash";
	}
	
	@PostMapping("/delete-all")
	public String deleteAll() {
		List<Pizza> pizzas = pizzaService.findAllTrashedPizzas();
		pizzaService.deleteAll(pizzas);
		return "redirect:/pizzas/trash";
	}
}
