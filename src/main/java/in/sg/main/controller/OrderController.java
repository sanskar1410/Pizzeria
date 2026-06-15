package in.sg.main.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.sg.main.entity.Order;
import in.sg.main.entity.User;
import in.sg.main.service.CartService;
import in.sg.main.service.OrderService;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	
	// PLACE ORDER - POST METHOD
	@PostMapping(value = "/order/place")
	public String placeOrder(
			@RequestParam(value = "deliveryAddress") String deliveryAddress,
			@RequestParam(value = "phoneNumber") String phoneNumber,
			HttpSession session,
			Model model) {
		
		System.out.println("========== PLACE ORDER ==========");
		System.out.println("deliveryAddress: " + deliveryAddress);
		System.out.println("phoneNumber: " + phoneNumber);
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + user);
		
		if (user == null) {
			System.out.println("User is NULL - redirecting to login");
			return "redirect:/login";
		}
		
		try {
			System.out.println("Calling orderService.placeOrder...");
			Order order = orderService.placeOrder(user, deliveryAddress, phoneNumber);
			System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
			model.addAttribute("successMessage", "Order placed successfully!");
			return "redirect:/order/view";
		} catch (Exception e) {
			System.out.println("ERROR placing order: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error: " + e.getMessage());
			return "redirect:/cart/view";
		}
	}
	
	// VIEW ORDERS - GET METHOD
	@GetMapping(value = "/order/view")
	public String viewOrders(HttpSession session, Model model) {
		
		System.out.println("========== VIEW ORDERS ==========");
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + (user != null ? user.getId() : "null"));
		
		if (user == null) {
			System.out.println("User is NULL - redirecting to login");
			return "redirect:/login";
		}
		
		try {
			List<Order> allOrders = new ArrayList<>();
			
			try {
				allOrders = orderService.getUserOrders(user);
				System.out.println("Orders retrieved from service: " + (allOrders != null ? allOrders.size() : 0));
			} catch (Exception e) {
				System.out.println("Error retrieving orders from service: " + e.getMessage());
				// If service fails, use empty list
				allOrders = new ArrayList<>();
			}
			
			// Ensure allOrders is never null
			if (allOrders == null) {
				allOrders = new ArrayList<>();
			}
			
			System.out.println("Total orders: " + allOrders.size());
			
			// Separate current and previous orders
			List<Order> currentOrders = new ArrayList<>();
			List<Order> previousOrders = new ArrayList<>();
			
			for (Order order : allOrders) {
				if (order != null && order.getOrderStatus() != null) {
					if (order.getOrderStatus().equals("Pending") || order.getOrderStatus().equals("Processing")) {
						currentOrders.add(order);
					} else if (order.getOrderStatus().equals("Delivered") || order.getOrderStatus().equals("Cancelled")) {
						previousOrders.add(order);
					}
				}
			}
			
			System.out.println("Current orders: " + currentOrders.size());
			System.out.println("Previous orders: " + previousOrders.size());
			
			model.addAttribute("user", user);
			model.addAttribute("currentOrders", currentOrders);
			model.addAttribute("previousOrders", previousOrders);
			model.addAttribute("allOrders", allOrders);
			model.addAttribute("hasOrders", !allOrders.isEmpty());
			
		} catch (Exception e) {
			System.out.println("ERROR in viewOrders: " + e.getMessage());
			e.printStackTrace();
			
			// Add empty lists on error
			model.addAttribute("user", user);
			model.addAttribute("currentOrders", new ArrayList<>());
			model.addAttribute("previousOrders", new ArrayList<>());
			model.addAttribute("allOrders", new ArrayList<>());
			model.addAttribute("hasOrders", false);
			model.addAttribute("error", "Error loading orders: " + e.getMessage());
		}
		
		return "order";
	}
	
	// CHECKOUT PAGE - GET METHOD
	@GetMapping(value = "/order/checkout")
	public String checkoutPage(HttpSession session, Model model) {
		
		System.out.println("========== CHECKOUT PAGE ==========");
		
		User user = (User) session.getAttribute("user");
		System.out.println("User from session: " + (user != null ? user.getId() : "null"));
		
		if (user == null) {
			System.out.println("User is NULL - redirecting to login");
			return "redirect:/login";
		}
		
		try {
			Double totalPrice = cartService.getCartTotal(user);
			System.out.println("Cart total: " + totalPrice);
			
			if (totalPrice == null) {
				totalPrice = 0.0;
			}
			
			model.addAttribute("user", user);
			model.addAttribute("totalPrice", totalPrice);
			
		} catch (Exception e) {
			System.out.println("ERROR in checkoutPage: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("totalPrice", 0.0);
			model.addAttribute("error", "Error loading checkout: " + e.getMessage());
		}
		
		return "checkout";
	}
}