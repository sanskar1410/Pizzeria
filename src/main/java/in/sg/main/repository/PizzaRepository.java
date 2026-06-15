package in.sg.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.sg.main.entity.Pizza;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
	
}
