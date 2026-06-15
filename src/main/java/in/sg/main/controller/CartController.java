package in.sg.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.sg.main.entity.Cart;
import in.sg.main.entity.User;
import in.sg.main.service.CartService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	// ADD TO CART - POST METHOD
	@PostMapping(value = "/cart/add")
	public String addToCart(
			@RequestParam(value = "pizzaId") Long pizzaId, 
			@RequestParam(value = "quantity") Integer quantity,
			HttpSession session,
			Model model) {
		
		System.out.println("========== ADD TO CART ==========");
		System.out.println("pizzaId: " + pizzaId);
		System.out.println("quantity: " + quantity);
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + user);
		
		if (user == null) {
			System.out.println("User is NULL - redirecting to login");
			return "redirect:/login";
		}
		
		try {
			System.out.println("Calling cartService.addItemToCart...");
			cartService.addItemToCart(user, pizzaId, quantity);
			System.out.println("Item added successfully!");
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error: " + e.getMessage());
			return "redirect:/menu?error=true";
		}
		
		System.out.println("Redirecting to /cart/view");
		return "redirect:/cart/view";
	}
	
	
	
	// VIEW CART - GET METHOD
	@GetMapping(value = "/cart/view")
	public String viewCart(HttpSession session, Model model) {
		
		System.out.println("========== VIEW CART ==========");
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + user);
		
		if (user == null) {
			System.out.println("User is NULL - redirecting to login");
			return "redirect:/login";
		}
		
		try {
			Cart cart = cartService.getCartByUser(user);
			System.out.println("Cart retrieved: " + cart);
			
			if (cart != null) {
				System.out.println("Cart has " + cart.getCartItems().size() + " items");
				System.out.println("Cart total: " + cart.getTotalPrice());
				
				model.addAttribute("cart", cart);
				model.addAttribute("cartItems", cart.getCartItems());
				model.addAttribute("totalPrice", cart.getTotalPrice());
				model.addAttribute("totalItems", cart.getTotalItems());
			} else {
				System.out.println("Cart is NULL - creating empty cart");
				model.addAttribute("cartItems", null);
				model.addAttribute("totalPrice", 0.0);
				model.addAttribute("totalItems", 0);
			}
			
			model.addAttribute("user", user);
			System.out.println("Cart page attributes set");
			
		} catch (Exception e) {
			System.out.println("ERROR in viewCart: " + e.getMessage());
			e.printStackTrace();
		}
		
		return "cart";
	}
	
	
	// REMOVE FROM CART - POST METHOD
	@PostMapping(value = "/cart/remove/{cartItemId}")
	public String removeFromCart(
			@PathVariable(value = "cartItemId") Long cartItemId,
			HttpSession session) {
		
		System.out.println("========== REMOVE FROM CART ==========");
		System.out.println("cartItemId: " + cartItemId);
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + user);
		
		if (user != null) {
			try {
				cartService.removeItemFromCart(user, cartItemId);
				System.out.println("Item removed successfully");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return "redirect:/cart/view";
	}
	
	// UPDATE CART ITEM - POST METHOD
	@PostMapping(value = "/cart/update/{cartItemId}")
	public String updateCartItem(
			@PathVariable(value = "cartItemId") Long cartItemId,
			@RequestParam(value = "quantity") Integer quantity) {
		
		System.out.println("========== UPDATE CART ITEM ==========");
		System.out.println("cartItemId: " + cartItemId);
		System.out.println("quantity: " + quantity);
		
		if (quantity > 0) {
			try {
				cartService.updateCartItem(cartItemId, quantity);
				System.out.println("Item updated successfully");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return "redirect:/cart/view";
	}
	
	// CLEAR CART - GET METHOD
	@GetMapping(value = "/cart/clear")
	public String clearCart(HttpSession session) {
		
		System.out.println("========== CLEAR CART ==========");
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + user);
		
		if (user != null) {
			try {
				cartService.clearCart(user);
				System.out.println("Cart cleared successfully");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return "redirect:/cart/view";
	}
}