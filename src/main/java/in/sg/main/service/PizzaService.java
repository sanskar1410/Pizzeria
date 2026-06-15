package in.sg.main.service;

import java.util.List;
import java.util.Optional;
import in.sg.main.entity.Pizza;

public interface PizzaService {
	List<Pizza> getAllPizzas();
	Optional<Pizza> getPizzaById(Long id);
	Pizza savePizza(Pizza pizza);
	void deletePizza(Long id);
}
