package in.sg.main.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.sg.main.entity.Cart;
import in.sg.main.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	List<Cart> findByUser(User user);
}
