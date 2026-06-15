package in.sg.main.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.sg.main.entity.CartItem;
import in.sg.main.entity.Cart;
import in.sg.main.entity.Pizza;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartAndPizza(Cart cart, Pizza pizza);
}
