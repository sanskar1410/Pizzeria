package in.sg.main.service;

import java.util.List;
import in.sg.main.entity.Order;
import in.sg.main.entity.User;

public interface OrderService {
	Order placeOrder(User user, String deliveryAddress, String phoneNumber);
	List<Order> getUserOrders(User user);
	Order getOrderById(Long orderId);
	void updateOrderStatus(Long orderId, String status);
}
