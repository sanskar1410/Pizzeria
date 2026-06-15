package in.sg.main.service_impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.sg.main.entity.Cart;
import in.sg.main.entity.CartItem;
import in.sg.main.entity.Order;
import in.sg.main.entity.OrderItem;
import in.sg.main.entity.User;
import in.sg.main.repository.CartRepository;
import in.sg.main.repository.OrderItemRepository;
import in.sg.main.repository.OrderRepository;
import in.sg.main.service.CartService;
import in.sg.main.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartService cartService;
	
	@Override
	public Order placeOrder(User user, String deliveryAddress, String phoneNumber) {
		List<Cart> carts = cartRepository.findByUser(user);

		if (carts.isEmpty()) {
		    throw new RuntimeException("Cart is empty");
		}

		// Get latest cart
		Cart cart = carts.stream()
		        .max((c1, c2) -> c1.getUpdatedDate().compareTo(c2.getUpdatedDate()))
		        .orElseThrow(() -> new RuntimeException("Cart not found"));

		if (cart.getCartItems().isEmpty()) {
		    throw new RuntimeException("Cart is empty");
		}
		
		Double totalAmount = cart.getTotalPrice();
		
		Order order = new Order(user, totalAmount);
		order.setDeliveryAddress(deliveryAddress);
		order.setPhoneNumber(phoneNumber);
		order = orderRepository.save(order);
		
		// Create order items from cart items
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem(order, cartItem.getPizza(), 
				cartItem.getQuantity(), cartItem.getPrice());
			orderItemRepository.save(orderItem);
			order.getOrderItems().add(orderItem);
		}
		
		// Clear the cart after order is placed
		cartService.clearCart(user);
		
		return orderRepository.save(order);
	}
	
	@Override
	public List<Order> getUserOrders(User user) {
		return orderRepository.findByUserOrderByOrderDateDesc(user);
	}
	
	@Override
	public Order getOrderById(Long orderId) {
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		return orderOpt.orElse(null);
	}
	
	@Override
	public void updateOrderStatus(Long orderId, String status) {
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isPresent()) {
			Order order = orderOpt.get();
			order.setOrderStatus(status);
			orderRepository.save(order);
		}
	}
}
