package org.java.spring;

import java.util.ArrayList;
import java.util.List;

import org.java.spring.pojo.Pizza;
import org.java.spring.services.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

@SpringBootApplication
public class SpringLaMiaPizzeriaCrudApplication implements CommandLineRunner {
	@Autowired
	private PizzaService pizzaService;

	public static void main(String[] args){
		SpringApplication.run(SpringLaMiaPizzeriaCrudApplication.class, args);
	}

	public void run(String... args) throws Exception{
		addToDb();
	}
	
	private List<Pizza> getPizzasList() {
		Faker faker = new Faker();
		int min = 10;
		int max = 100;
		int rndNumber = faker.number().numberBetween(min,max);
		List<Pizza> pizzasList = new ArrayList<>();
		
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
	
	private void addToDb() {		
		for(Pizza p : getPizzasList()) {
			pizzaService.save(p);
		}
	}

}
