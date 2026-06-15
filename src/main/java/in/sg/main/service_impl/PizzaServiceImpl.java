package in.sg.main.service_impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.sg.main.entity.Pizza;
import in.sg.main.repository.PizzaRepository;
import in.sg.main.service.PizzaService;

@Service
public class PizzaServiceImpl implements PizzaService {
	
	@Autowired
	private PizzaRepository pizzaRepository;
	
	@Override
	public List<Pizza> getAllPizzas() {
		return pizzaRepository.findAll();
	}
	
	@Override
	public Optional<Pizza> getPizzaById(Long id) {
		return pizzaRepository.findById(id);
	}
	
	@Override
	public Pizza savePizza(Pizza pizza) {
		return pizzaRepository.save(pizza);
	}
	
	@Override
	public void deletePizza(Long id) {
		pizzaRepository.deleteById(id);
	}
}
