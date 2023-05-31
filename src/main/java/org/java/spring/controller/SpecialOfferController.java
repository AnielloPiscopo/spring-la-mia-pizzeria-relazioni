package org.java.spring.controller;

import java.util.List;
import java.util.Optional;

import org.java.spring.pojo.Pizza;
import org.java.spring.pojo.SpecialOffer;
import org.java.spring.services.PizzaService;
import org.java.spring.services.SpecialOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/special-offers")
public class SpecialOfferController {
	private static String pageTitle;
	
	private String getSpecialOffers(List<SpecialOffer> specialOffers , String title , String template , Model model) {
		model.addAttribute("specialOffers" , specialOffers);
		model.addAttribute("title" , title);
		return template;
	}
	
	private String saveInDb(SpecialOffer specialOffer , List<Pizza> pizzas , BindingResult br , String templateToEdit , String templateToRedirect , String title , String btnText , Model model) {
		if(br.hasErrors()) {
			model.addAttribute("specialOffer" , specialOffer);
			model.addAttribute("errors" , br);
			modifyOrCreateSpecialOffer(specialOffer, pizzas , title, btnText, templateToRedirect, model);
			return templateToEdit;
		}
		
		service.save(specialOffer);
		return templateToRedirect;
	}
	
	private String modifyOrCreateSpecialOffer(SpecialOffer specialOffer , List<Pizza> pizzas , String title , String btnText , String template , Model model) {
		model.addAttribute("btnText" , btnText);
		model.addAttribute("specialOffer", specialOffer);
		model.addAttribute("pizzas" , pizzas);
		model.addAttribute("title" , title);
		return template;
	}
	
	private String changeTheDeletedValue(int id , boolean trashed) {
		Optional<SpecialOffer> optSpecialOffer = service.findById(id);
		SpecialOffer specialOffer = optSpecialOffer.get();
		specialOffer.setDeleted(trashed);
		service.save(specialOffer);
		return "redirect:/special-offers/";
	}
	
	@Autowired
	private SpecialOfferService service;
	
	@Autowired
	private PizzaService pizzaService;
	
	@GetMapping("/")
	public String index(Model model ) {
		List<SpecialOffer> specialOffers = service.findAllAvailableSpecialOffers();
		return getSpecialOffers(specialOffers , "Lista offerte speciali" , "special-offer/index" , model);
	}
	
	@GetMapping("/{id}")
	public String show(Model model , @PathVariable("id") int id) {
		Optional<SpecialOffer> optSpecialOffer = service.findById(id);
		SpecialOffer specialOffer = optSpecialOffer.get();
		pageTitle = "Offerta speciale: " + specialOffer.getTitle();
		model.addAttribute("specialOffer" , specialOffer);
		model.addAttribute("title" , pageTitle);
		return "special-offer/show";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		return modifyOrCreateSpecialOffer(new SpecialOffer() , pizzas , "Creazione offerta speciale" , "Aggiungi alla lista l'offerta" , "special-offer/create" , model);
	}
	
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("special-offer") SpecialOffer specialOffer , BindingResult br , Model model) {
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		return saveInDb(specialOffer, pizzas, br , "special-offer/create" ,  "redirect:/special-offers/" , "Creazione offerta" , "Aggiungi alla lista l'offerta" , model);
	}
	
	@GetMapping("/edit/{id}")
	public String edit(Model model , @PathVariable("id") int id) {
		Optional<SpecialOffer> optSpecialOffer = service.findById(id);
		SpecialOffer specialOffer = optSpecialOffer.get();
		pageTitle = "Modifica l'offerta : " + specialOffer.getTitle();
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		return modifyOrCreateSpecialOffer(specialOffer , pizzas , pageTitle , "Modifica elemento" , "special-offer/edit" , model);
	}
	
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("special-offer") SpecialOffer specialOffer , BindingResult br , Model model) {
		pageTitle = "Modifica l'offerta: " + specialOffer.getTitle();
		List<Pizza> pizzas = pizzaService.findAllAvailablePizzas();
		return saveInDb(specialOffer, pizzas , br , "special-offer/edit" , "redirect:/special-offers/" + specialOffer.getId() , pageTitle , "Modifica elemento" , model);
	}
	
	@PostMapping("/soft-delete/{id}")
	public String softDelete(@PathVariable("id") int id) {
		return changeTheDeletedValue(id, true);
	}
	
	@PostMapping("/soft-delete-all")
	public String softDeleteAll() {
		List<SpecialOffer> specialOffers = service.findAllAvailableSpecialOffers();
		for(SpecialOffer so : specialOffers) {
			so.setDeleted(true);
			service.save(so);
		}
		return "redirect:/special-offers/";
	}
	
	@GetMapping("/trash")
	public String trash(Model model ) {
		List<SpecialOffer> specialOffers = service.findAllTrashedSpecialOffers();
		return getSpecialOffers(specialOffers , "Lista offerte cestinate" , "special-offer/trash" , model);
	}
	
	@PostMapping("/refresh/{id}")
	public String refresh(@PathVariable("id") int id) {
		return changeTheDeletedValue(id, false);
	}
	
	@PostMapping("/refresh-all")
	public String refreshAll() {
		List<SpecialOffer> specialOffer = service.findAllTrashedSpecialOffers();
		for(SpecialOffer so : specialOffer) {
			so.setDeleted(false);
			service.save(so);
		}
		return "redirect:/special-offers/trash";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		Optional<SpecialOffer> optSpecialOffer = service.findById(id);
		SpecialOffer specialOffer = optSpecialOffer.get();
		service.delete(specialOffer);
		return "redirect:/special-offers/trash";
	}
	
	@PostMapping("/delete-all")
	public String deleteAll() {
		List<SpecialOffer> specialOffers = service.findAllTrashedSpecialOffers();
		service.deleteAll(specialOffers);
		return "redirect:/special-offers/trash";
	}
}
