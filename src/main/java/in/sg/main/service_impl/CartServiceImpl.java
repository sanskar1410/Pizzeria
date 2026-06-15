package in.sg.main.service_impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.sg.main.entity.Cart;
import in.sg.main.entity.CartItem;
import in.sg.main.entity.Pizza;
import in.sg.main.entity.User;
import in.sg.main.repository.CartItemRepository;
import in.sg.main.repository.CartRepository;
import in.sg.main.repository.PizzaRepository;
import in.sg.main.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;
	
	@Override
	public Cart getCartByUser(User user) {
		System.out.println("========== GET CART BY USER ==========");
		System.out.println("User ID: " + user.getId());
		
		// Get ALL carts for this user
		List<Cart> allCarts = cartRepository.findByUser(user);
		System.out.println("Total carts found: " + (allCarts != null ? allCarts.size() : 0));
		
		if (allCarts == null || allCarts.isEmpty()) {
			System.out.println("No carts found");
			return null;
		}
		
		// Get the LATEST cart (most recently updated)
		Cart latestCart = allCarts.stream()
			.max((c1, c2) -> c1.getUpdatedDate().compareTo(c2.getUpdatedDate()))
			.orElse(null);
		
		System.out.println("Latest cart ID: " + (latestCart != null ? latestCart.getCartId() : "null"));
		
		// DELETE all other old carts (cleanup)
		if (allCarts.size() > 1 && latestCart != null) {
			for (Cart cart : allCarts) {
				if (cart.getCartId() != latestCart.getCartId()) {
					System.out.println("Deleting old cart: " + cart.getCartId());
					// Delete all items in the old cart first
					cart.getCartItems().clear();
					cartRepository.delete(cart);
				}
			}
		}
		
		return latestCart;
	}
	
	@Override
	public Cart createCart(User user) {
		System.out.println("========== CREATE NEW CART ==========");
		System.out.println("User ID: " + user.getId());
		
		// First check if user already has a cart
		List<Cart> existingCarts = cartRepository.findByUser(user);
		if (existingCarts != null && !existingCarts.isEmpty()) {
			System.out.println("User already has " + existingCarts.size() + " cart(s)");
			
			// Get the latest one
			Cart latestCart = existingCarts.stream()
				.max((c1, c2) -> c1.getUpdatedDate().compareTo(c2.getUpdatedDate()))
				.orElse(null);
			
			if (latestCart != null) {
				System.out.println("Returning existing cart: " + latestCart.getCartId());
				
				// Delete other carts
				for (Cart cart : existingCarts) {
					if (cart.getCartId() != latestCart.getCartId()) {
						System.out.println("Deleting duplicate cart: " + cart.getCartId());
						cart.getCartItems().clear();
						cartRepository.delete(cart);
					}
				}
				
				return latestCart;
			}
		}
		
		// Create new cart ONLY if user has no carts
		System.out.println("Creating new cart for user");
		Cart cart = new Cart(user);
		Cart savedCart = cartRepository.save(cart);
		System.out.println("New cart created with ID: " + savedCart.getCartId());
		return savedCart;
	}
	
	@Override
	public void addItemToCart(User user, Long pizzaId, Integer quantity) {
		System.out.println("========== ADD ITEM TO CART ==========");
		System.out.println("User ID: " + user.getId());
		System.out.println("Pizza ID: " + pizzaId);
		System.out.println("Quantity: " + quantity);
		
		// Get existing cart OR create one
		Cart cart = getCartByUser(user);
		
		if (cart == null) {
			System.out.println("No existing cart found, creating new one");
			cart = createCart(user);
		}
		
		System.out.println("Using cart ID: " + cart.getCartId());
		
		// Get pizza
		Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaId);
		if (!pizzaOpt.isPresent()) {
			System.out.println("ERROR: Pizza not found with ID: " + pizzaId);
			throw new RuntimeException("Pizza not found with ID: " + pizzaId);
		}
		
		Pizza pizza = pizzaOpt.get();
		System.out.println("Pizza: " + pizza.getPizzaName() + ", Price: " + pizza.getPrice());
		
		// Check if item already exists in cart
		Optional<CartItem> existingItem = cartItemRepository.findByCartAndPizza(cart, pizza);
		
		if (existingItem.isPresent()) {
			System.out.println("Item already in cart, updating quantity");
			CartItem item = existingItem.get();
			Integer oldQty = item.getQuantity();
			item.setQuantity(oldQty + quantity);
			cartItemRepository.save(item);
			System.out.println("Quantity updated from " + oldQty + " to " + item.getQuantity());
		} else {
			System.out.println("Item not in cart, adding new item");
			CartItem cartItem = new CartItem(cart, pizza, quantity);
			cartItem.setPrice(pizza.getPrice());
			cartItemRepository.save(cartItem);
			System.out.println("New cart item added");
		}
		
		// Update cart timestamp
		cart.setUpdatedDate(LocalDateTime.now());
		cartRepository.save(cart);
		System.out.println("Cart updated at: " + cart.getUpdatedDate());
	}
	
	@Override
	public void removeItemFromCart(User user, Long cartItemId) {
		System.out.println("========== REMOVE ITEM FROM CART ==========");
		System.out.println("CartItem ID: " + cartItemId);
		
		Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
		if (cartItemOpt.isPresent()) {
			cartItemRepository.deleteById(cartItemId);
			System.out.println("Item removed");
			
			// Update cart timestamp
			Cart cart = getCartByUser(user);
			if (cart != null) {
				cart.setUpdatedDate(LocalDateTime.now());
				cartRepository.save(cart);
				System.out.println("Cart updated");
			}
		} else {
			System.out.println("ERROR: CartItem not found with ID: " + cartItemId);
		}
	}
	
	@Override
	public void updateCartItem(Long cartItemId, Integer quantity) {
		System.out.println("========== UPDATE CART ITEM ==========");
		System.out.println("CartItem ID: " + cartItemId);
		System.out.println("New Quantity: " + quantity);
		
		Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
		if (cartItemOpt.isPresent()) {
			CartItem item = cartItemOpt.get();
			if (quantity > 0) {
				item.setQuantity(quantity);
				cartItemRepository.save(item);
				System.out.println("Item quantity updated");
				
				// Update cart timestamp
				Cart cart = item.getCart();
				cart.setUpdatedDate(LocalDateTime.now());
				cartRepository.save(cart);
				System.out.println("Cart updated");
			}
		} else {
			System.out.println("ERROR: CartItem not found with ID: " + cartItemId);
		}
	}
	
	@Override
	public void clearCart(User user) {
		System.out.println("========== CLEAR CART ==========");
		System.out.println("User ID: " + user.getId());
		
		Cart cart = getCartByUser(user);
		if (cart != null) {
			System.out.println("Clearing cart: " + cart.getCartId());
			// Delete all items
			cart.getCartItems().clear();
			cartRepository.save(cart);
			System.out.println("Cart cleared");
		} else {
			System.out.println("No cart to clear");
		}
	}
	
	@Override
	public Double getCartTotal(User user) {
		System.out.println("========== GET CART TOTAL ==========");
		System.out.println("User ID: " + user.getId());
		
		Cart cart = getCartByUser(user);
		if (cart != null) {
			Double total = cart.getTotalPrice();
			System.out.println("Cart total: " + total);
			return total;
		}
		
		System.out.println("No cart found, returning 0.0");
		return 0.0;
	}
}