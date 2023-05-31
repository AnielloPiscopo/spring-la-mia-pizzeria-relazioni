package org.java.spring;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.java.spring.pojo.Pizza;
import org.java.spring.pojo.SpecialOffer;
import org.java.spring.services.PizzaService;
import org.java.spring.services.SpecialOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

@SpringBootApplication
public class SpringLaMiaPizzeriaCrudApplication implements CommandLineRunner {
	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private SpecialOfferService specialOfferService;
	
	private static List<Pizza> pizzasList = new ArrayList<>();

	public static void main(String[] args){
		SpringApplication.run(SpringLaMiaPizzeriaCrudApplication.class, args);
	}

	public void run(String... args) throws Exception{
		addToDb();
	}
	
	private List<Pizza> createPizzasList() {
		Faker faker = new Faker();
		int min = 10;
		int max = 100;
		int rndNumber = faker.number().numberBetween(min,max);
		
		for(int i=1 ; i<rndNumber ; i++) {
			String rndName = String.join(", " , faker.lorem().words());
			String rndDescription = faker.lorem().sentence(min);
			String rndImgUrl = faker.internet().image();
			float rndPrice = (float) faker.number().randomDouble(2, 1, max);
			Pizza rndPizza = new Pizza(rndName , rndDescription , rndImgUrl , rndPrice);
			pizzasList.add(rndPizza);
		}
		
		return pizzasList;
	}
	
	private List<SpecialOffer> createSpecialOffersList(List<Pizza> pizzas){
		Faker faker = new Faker();
		int min = 1;
		int max = 30;
		int minDiscount = 5;
		int maxDiscount = 30;
		int rndNumber = faker.number().numberBetween(min,max);
		int pizzasSize = pizzas.size();
		List<SpecialOffer> specialOffersList = new ArrayList<>();
		
		for(int i=1 ; i<rndNumber ; i++) {
			String rndTitle = String.join(", " , faker.lorem().words());
			LocalDate rndStartDate = LocalDate.parse("2007-12-03");
			LocalDate rndEndDate = LocalDate.parse("2007-12-03");
			int rndDiscount = faker.number().numberBetween(minDiscount, maxDiscount);
			int rndPizzasIndex = faker.number().numberBetween(0, pizzasSize);
			Pizza selectedPizza = pizzas.get(rndPizzasIndex);
			SpecialOffer rndSpecialOffer = new SpecialOffer(selectedPizza , rndTitle , rndStartDate , rndEndDate , rndDiscount);
			specialOffersList.add(rndSpecialOffer);
		}
		
		return specialOffersList;
	}
	
	private void addToDb() {
		for(Pizza p : createPizzasList()) {
			pizzaService.save(p);
		}
		
		for(SpecialOffer so : createSpecialOffersList(pizzasList)) {
			specialOfferService.save(so);
		}
	}

}
