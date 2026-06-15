package in.sg.main.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sg.main.entity.Pizza;
import in.sg.main.entity.User;
import in.sg.main.service.PizzaService;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	@Autowired
	private PizzaService pizzaService;
	
	@GetMapping("/index")
	public String index(HttpSession session, Model model) {
		System.out.println("========== INDEX PAGE ==========");
		
		// Check if user is in session
		User user = (User) session.getAttribute("user");
		System.out.println("User in session: " + user);
		
		// Add user to model (might be null if not logged in)
		model.addAttribute("user", user);
		
		return "index";
	}
	
	@GetMapping("/menu")
	public String menu(HttpSession session, Model model) {
		System.out.println("========== MENU PAGE ==========");
		
		// Check if user is in session
		User user = (User) session.getAttribute("user");
		System.out.println("User in session: " + (user != null ? user.getId() : "null"));
		
		try {
			// Get all pizzas
			List<Pizza> pizzas = pizzaService.getAllPizzas();
			System.out.println("Pizzas loaded: " + pizzas.size());
			
			model.addAttribute("pizzas", pizzas);
			model.addAttribute("user", user); // Add user to model
			
		} catch (Exception e) {
			System.out.println("ERROR loading pizzas: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("pizzas", null);
			model.addAttribute("user", user);
		}
		
		return "menu";
	}
	
	@GetMapping("/about")
	public String about(HttpSession session, Model model) {
		System.out.println("========== ABOUT PAGE ==========");
		
		// Check if user is in session
		User user = (User) session.getAttribute("user");
		System.out.println("User in session: " + (user != null ? user.getId() : "null"));
		
		model.addAttribute("user", user);
		
		return "about";
	}
}