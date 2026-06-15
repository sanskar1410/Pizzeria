package in.sg.main.service;

import java.util.Optional;
import in.sg.main.entity.Cart;
import in.sg.main.entity.User;

public interface CartService {
	Cart getCartByUser(User user);
	Cart createCart(User user);
	void addItemToCart(User user, Long pizzaId, Integer quantity);
	void removeItemFromCart(User user, Long pizzaItemId);
	void updateCartItem(Long cartItemId, Integer quantity);
	void clearCart(User user);
	Double getCartTotal(User user);
}
